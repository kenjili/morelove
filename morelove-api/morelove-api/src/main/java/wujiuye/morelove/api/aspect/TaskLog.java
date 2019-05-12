package wujiuye.morelove.api.aspect;


import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class TaskLog {
    /**
     * 定义切点
     */
    @Pointcut("execution(* wujiuye.morelove.api.tasks.*..*.*(..))")
    private void taskLogPointcut() {
    }

    /**
     * 前置通知
     */
    @Before("taskLogPointcut()")
    public void beforeLog(JoinPoint joinPoint) {
        log.info("======== 开始执行异步任务：" + joinPoint.getSignature().getName() + " =======");
    }

    /**
     * 后置异常通知
     */
    @AfterThrowing(value = "taskLogPointcut()", throwing = "ex")
    public void throwingLog(JoinPoint joinPoint, Exception ex) {
        log.error("异步任务 ==> " + joinPoint.getSignature().getName() +
                " 出现异常==>" + ex);
    }
}
