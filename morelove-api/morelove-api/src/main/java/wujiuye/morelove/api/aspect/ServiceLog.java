package wujiuye.morelove.api.aspect;


import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;


/**
 * 日记切面类,监听业务层执行方法
 *
 * @author wjy
 */
@Aspect
@Component
@Slf4j
public class ServiceLog {

    /**
     * 定义切点
     */
    @Pointcut("execution(* wujiuye.morelove.api.service.*..*.*(..))")
    private void logPointcut() {
    }

    /**
     * 前置通知
     */
    @Before("logPointcut()")
    public void beforeLog(JoinPoint joinPoint) {
        log.info("========目标方法（" + joinPoint.getSignature().getName() + "）执行之前记录日记=======");
    }

    /**
     * 后置异常通知
     */
    @AfterThrowing(value = "logPointcut()", throwing = "ex")
    public void throwingLog(JoinPoint joinPoint, Exception ex) {
        String methodName = joinPoint.getSignature().getName();
        log.error("目标方法名==>" + methodName +
                " 异常==>" + ex);
    }

}
