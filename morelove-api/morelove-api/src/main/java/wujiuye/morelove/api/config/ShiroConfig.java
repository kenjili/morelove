package wujiuye.morelove.api.config;

import wujiuye.morelove.api.shiro.RolesAuthorizationFilter;
import wujiuye.morelove.api.shiro.UserAuthcFilter;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionListener;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.session.mgt.ValidatingSessionManager;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.apache.shiro.mgt.SecurityManager;

import javax.servlet.Filter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Configuration
@Slf4j
public class ShiroConfig {

    @Autowired
    private AuthorizingRealm realm;

    /**
     * 配置SecurityManager
     * @return
     */
    @Bean
    public SecurityManager securityManager(@Autowired SessionManager sessionManager) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(realm);
        //配置sessionManager，securityManager管理sessionManager，否则sessionManager不生效
        securityManager.setSessionManager(sessionManager);
        return securityManager;
    }

    /**
     * 被代理的shiroFilter
     * @param securityManager
     * @return
     */
    @Bean("shiroFilter")
    public ShiroFilterFactoryBean shiroFilterFactory(@Qualifier("securityManager") SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        //注入SecurityManager
        shiroFilterFactoryBean.setSecurityManager(securityManager);

        //注入过滤器
        Map<String, Filter> filterMap = new HashMap<>();
        filterMap.put("authc", new UserAuthcFilter());
        filterMap.put("roles", new RolesAuthorizationFilter());
        shiroFilterFactoryBean.setFilters(filterMap);

        //过滤器Chain定义,指定哪些资源（url）交给哪个过滤器过滤，责任链模式
        Map<String, String> map = new HashMap<>();
        map.put("/", "anon");
        map.put("/index/*","anon");
        map.put("/static/**","anon");//静态资源
        map.put("/sms/send", "anon");
        map.put("/user/login", "anon");
        map.put("/user/regist", "anon");
        map.put("/user/logout", "authc");
        map.put("/lover/record", "roles[lover]");
        map.put("/lover/*", "authc");
        map.put("/found/*", "authc");
        map.put("/**", "roles[lover]");

        shiroFilterFactoryBean.setFilterChainDefinitionMap(map);
        return shiroFilterFactoryBean;
    }


    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  session管理 >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    /**
     * 实现session存储的dao层，我们可以通过自定义一个实现SessionDAO接口的类来管理Session的存储，
     * 后面讲集群配置的时候使用redis实现session共享再来实现自定义SessionDAO，现在暂时使用shiro
     * 提供的内存缓存MemorySessionDAO
     * @return
     */
//    @Bean
//    public SessionDAO sessionDAO() {
//        /**
//         * 暂时使用内存存储session，后面改成redis
//         * MemorySessionDAO是将session缓存在内存中的，使用ConcurrentHashMap保存，key为sessionId,
//         * value为Session，使用ConcurrentHashMap保证了线程安全
//         * private ConcurrentMap<Serializable, Session> sessions = new ConcurrentHashMap();
//         *
//         */
//        MemorySessionDAO sessionDAO = new MemorySessionDAO();
//        /**
//         * 配置sessionId产生者，shiro会调用SessionIdGenerator的generateId方法
//         * 获取一个sessionId
//         * shiro提供了一个默认的实现类JavaUuidSessionIdGenerator，其generateId方法
//         * 就是返回一个随机的UUID作为sessionId，在次埋坑，后面填坑
//         * public Serializable generateId(Session session) {
//         *         return UUID.randomUUID().toString();
//         * }
//         */
//        sessionDAO.setSessionIdGenerator(new JavaUuidSessionIdGenerator());
//        return sessionDAO;
//    }

    /**
     * 这里需要设置一个cookie的名称  原因就是会跟原来的session的id值重复，tomcat生成的sessionId的key为JSESSIONID。
     * 解答疑问：tomcat与shiro的session关系？
     *      两者都包装的HttpSession, 作用都是做会话中服务器端存储会话数据之用；
     *      ShiroHttpServletRequest继承HttpServletRequestWrapper重写了getSession方法，将HttpSession
     *      包装成了ShiroHttpSession，在mvc控制器中获取Session打印出来的结果是ShiroHttpSession。
     *
     * 引用别人说的："设置Cookie名字, 默认为: JSESSIONID 问题: 与SERVLET容器名冲突, 如JETTY, TOMCAT 等默认JSESSIONID,
     *              当跳出SHIRO SERVLET时如ERROR-PAGE容器会为JSESSIONID重新分配值导致登录会话丢失!"。
     *
     * @return
     */
    @Bean
    public SimpleCookie simpleCookie() {
        /**
         * DefaultWebSessionManager内部默认创建了一个Cookie。
         * Cookie cookie = new SimpleCookie("JSESSIONID");
         * 而cookie的name就是JSESSIONID，所以我们需要自己提供一个
         * 自命名的SimpleCookie
         */
        SimpleCookie simpleCookie = new SimpleCookie("SHIROSESSION");
        /**
         * 浏览器中通过document.cookie可以获取cookie属性，
         * 设置了HttpOnly=true那么在脚本中就不能获取到cookie（document.cookie为null）
         * ，以此配置来避免cookie被盗用
         */
        simpleCookie.setHttpOnly(true);
        return simpleCookie;
    }


    /**
     * 管理Session的SessionManager，这里使用ValidatingSessionManager，ValidatingSessionManager
     * 接口继承SessionManager接口，提供了validateSessions方法来实现验证session的有效性。
     * @param sessionDAO
     * @return
     */
    @Bean
    public ValidatingSessionManager sessionManager(@Autowired SessionDAO sessionDAO,@Autowired SimpleCookie simpleCookie){
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        sessionManager.setSessionDAO(sessionDAO);
        sessionManager.setSessionIdCookie(simpleCookie);
        /**
         * setCacheManager方法配置CacheManager不是必要的，如果你使用磁盘存储的方式来保存Session，
         * 那么你才要考虑缓存，因为磁盘读写数度慢，但是不是直接给sessionManager写入缓存管理者就完事了的
         * 你需要为你实现的SessionDAO实现一个CacheManagerAware接口，如果SessionDAO是自己定义的，
         * 那还不如自己实现缓存管理就行了，还需要在sessionManager配置缓存管理者吗？多此一举。
         * private void applyCacheManagerToSessionDAO() {
         *         if (this.cacheManager != null && this.sessionDAO != null && this.sessionDAO instanceof CacheManagerAware) {
         *             ((CacheManagerAware)this.sessionDAO).setCacheManager(this.cacheManager);
         *         }
         *
         *     }
         */
        //sessionManager.setCacheManager();

        /**
         * 添加Session监听器，可选的。我们可以通过添加SessionListener
         * 获取当前session总数，实现计算在线人数的功能。
         */
        ArrayList<SessionListener> listeners = new ArrayList<>();
        listeners.add(new SessionListener() {

            private final AtomicInteger count = new AtomicInteger(0);

            /**
             * 会话创建时调用
             * @param session
             */
            @Override
            public void onStart(Session session) {
                int current = count.incrementAndGet();
                log.debug("当前在线人数："+current);
            }

            /**
             * 会话销毁时调用（一般时退出登录触发，所以与onExpiration不冲突）
             * @param session
             */
            @Override
            public void onStop(Session session) {
                int current = count.decrementAndGet();
                log.debug("当前在线人数："+current);
            }

            /**
             * session过期时调用
             * @param session
             */
            @Override
            public void onExpiration(Session session) {
                int current = count.decrementAndGet();
                log.debug("当前在线人数："+current);
            }
        });
        sessionManager.setSessionListeners(listeners);
        return sessionManager;
    }

    /**
     * 定时验证session过期的任务调度器
     * public interface SessionValidationScheduler {
     *     //是否启用
     *     boolean isEnabled();
     *     //启用session验证
     *     void enableSessionValidation();
     *     //禁用session验证
     *     void disableSessionValidation();
     * }
     * @param sessionManager
     * @return
     */
//    @Bean
//    public SessionValidationScheduler sessionValidationScheduler(@Autowired ValidatingSessionManager sessionManager){
//        ExecutorServiceSessionValidationScheduler validationScheduler = new ExecutorServiceSessionValidationScheduler();
//        //设置执行任务的时间间隔
//        validationScheduler.setInterval(1800_000L);
//        //设置session管理者
//        validationScheduler.setSessionManager(sessionManager);
//        return validationScheduler;
//    }

}
