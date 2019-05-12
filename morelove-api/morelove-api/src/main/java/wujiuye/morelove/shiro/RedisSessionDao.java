package wujiuye.morelove.shiro;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.SimpleSession;
import org.apache.shiro.session.mgt.eis.JavaUuidSessionIdGenerator;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.session.mgt.eis.SessionIdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

/**
 * 实现使用redis缓存session的SessionDAO
 *
 * @author wjy
 */
@Component
@Slf4j
public class RedisSessionDao implements SessionDAO {

    private static final String REDIS_CACHE_SESSION_KEY = "cache_session_key";
    private SessionIdGenerator sessionIdGenerator = new JavaUuidSessionIdGenerator();

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * session创建时调用,返回sessionId
     *
     * @param session
     * @return
     */
    @Override
    public Serializable create(Session session) {
        Serializable sessionId = sessionIdGenerator.generateId(session);
        ((SimpleSession) session).setId(sessionId);
        BoundHashOperations hashOperations = redisTemplate.boundHashOps(REDIS_CACHE_SESSION_KEY);
        hashOperations.put(sessionId, session);
        log.info("create session is ===> "+ session.getId());
        return sessionId;
    }

    /**
     * 返回session
     * 从redis中读取session
     *
     * @param sessionId
     * @return
     * @throws UnknownSessionException
     */
    @Override
    public Session readSession(Serializable sessionId) throws UnknownSessionException {
        BoundHashOperations hashOperations = redisTemplate.boundHashOps(REDIS_CACHE_SESSION_KEY);
        Session session = (Session) hashOperations.get(sessionId);
        log.info("read session is "+ (session==null?"null":session.getId()));
        return session;
    }

    /**
     * 更新session，刷新redis中的记录
     *
     * @param session
     * @throws UnknownSessionException
     */
    @Override
    public void update(Session session) throws UnknownSessionException {
        log.info("update session is ===>"+session.getId());
        BoundHashOperations hashOperations = redisTemplate.boundHashOps(REDIS_CACHE_SESSION_KEY);
        hashOperations.put(session.getId(), session);
    }

    /**
     * 删除session，将session从redis中移除
     *
     * @param session
     */
    @Override
    public void delete(Session session) {
        log.info("delete session is ===>"+session.getId());
        BoundHashOperations hashOperations = redisTemplate.boundHashOps(REDIS_CACHE_SESSION_KEY);
        hashOperations.delete(session.getId());
    }

    /**
     * 获取活动的session，未过期的
     * 从redis中读取未过期的，并将过期的移除掉
     *
     * @return
     */
    @Override
    public Collection<Session> getActiveSessions() {
        log.info("getActiveSessions===>");
        ArrayList<Session> activeSessions = new ArrayList<>();
        BoundHashOperations hashOperations = redisTemplate.boundHashOps(REDIS_CACHE_SESSION_KEY);
        java.util.Set keys = hashOperations.keys();
        long currentTime = System.currentTimeMillis();
        for (Object sid : keys) {
            SimpleSession session = (SimpleSession) hashOperations.get(sid);
            if (currentTime - session.getLastAccessTime().getTime() > session.getTimeout()) {
                continue;
            }
            activeSessions.add(session);
        }
        return activeSessions;
    }
}
