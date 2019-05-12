package wujiuye.morelove.api.shiro;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.session.mgt.ValidatingSessionManager;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;


/**
 * 调度任务，执行验证session，剔除过期session
 * @author wjy
 *
 */
@Component
@Slf4j
public class SessionValidationJob extends QuartzJobBean {

    //自动注入获取shiro的SessionManager
    @Autowired
    private SessionManager sessionManager;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("开始执行定时任务===>验证session，删除无效的session");
        if(sessionManager instanceof ValidatingSessionManager){
            log.debug("调用ValidatingSessionManager的validateSessions方法！");
            ((ValidatingSessionManager) sessionManager).validateSessions();
        }
    }
}
