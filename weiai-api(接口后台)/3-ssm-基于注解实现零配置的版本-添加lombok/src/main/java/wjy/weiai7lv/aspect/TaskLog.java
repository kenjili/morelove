package wjy.weiai7lv.aspect;


import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * Aspect只支持接口方式的aop
 */
@Aspect
@Component
public class TaskLog {

    private Logger logger = Logger.getLogger(this.getClass().getName());

    /**
     * 定义切点
     */
    @Pointcut("execution(* wjy.weiai7lv.tasks.*..*.*(..))")
    private void taskLogPointcut() {
    }

    /**
     * 前置通知
     */
    @Before("taskLogPointcut()")
    public void beforeLog(JoinPoint joinPoint) {
        logger.info("======== 开始执行异步任务：" + joinPoint.getSignature().getName() + " =======");
    }

    /**
     * 后置异常通知
     */
    @AfterThrowing(value = "taskLogPointcut()", throwing = "ex")
    public void throwingLog(JoinPoint joinPoint, Exception ex) {
        logger.error("异步任务 ==> " + joinPoint.getSignature().getName() +
                " 出现异常==>" + ex);
    }
}
