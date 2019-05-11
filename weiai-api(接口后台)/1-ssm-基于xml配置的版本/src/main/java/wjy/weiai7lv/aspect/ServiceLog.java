package wjy.weiai7lv.aspect;


import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;


/**
 * 日记切面类,监听业务层执行方法
 *
 * @author wjy
 */
@Aspect
@Component
public class ServiceLog {

    private Logger logger = Logger.getLogger(this.getClass().getName());

    /**
     * 定义切点
     */
    @Pointcut("execution(* wjy.weiai7lv.service.*..*.*(..))")
    private void logPointcut() {
    }

    /**
     * 前置通知
     */
    @Before("logPointcut()")
    public void beforeLog(JoinPoint joinPoint) {
        logger.info("========目标方法（" + joinPoint.getSignature().getName() + "）执行之前记录日记=======");
    }


//    /**
//     * 后置通知
//     */
//    @After("logPointcut()")
//    public void afterLog(JoinPoint joinPoint) {
//        aspect.info("========目标方法（" + joinPoint.getSignature().getName() + "）执行之后记录日记=======");
//    }

//    /**
//     * 后置返回通知
//     */
//    @AfterReturning(value = "logPointcut()", returning = "result")
//    public void returningLog(JoinPoint joinPoint, Object result) {
//        String methodName = joinPoint.getSignature().getName();
//        aspect.info("目标方法名==>" + methodName +
//                " 返回值==> " + result);
//    }

    /**
     * 后置异常通知
     */
    @AfterThrowing(value = "logPointcut()", throwing = "ex")
    public void throwingLog(JoinPoint joinPoint, Exception ex) {
        String methodName = joinPoint.getSignature().getName();
        logger.error("目标方法名==>" + methodName +
                " 异常==>" + ex);
    }

}
