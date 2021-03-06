package wujiuye.morelove.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 自定义全局异常处理器
 * 系统的dao、service、controller出现异常都是通过throws exception向上抛出的，
 * 最后spring mvc前端控制器将异常交由异常处理器进行处理。
 * 使用@ControllerAdvice注解将该类声明为Controller的切面类，spring-mvc会将其切入到所有@Controller声明的bean的所有public方法中,切点由spring-mvc定义
 * 具体的通知／增强也是由spring-mvc实现，在后置异常通知中根据异常类型来调用我们使用@ExceptionHandler声明的方法
 */
@ControllerAdvice //xml配置方式不要忘记在<context:component-scan>标签中添加包扫描
@Slf4j
public class WebExceptionHandler {

    /**
     * 统一处理自定义的异常
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(WebException.class)
    @ResponseBody
    public WebResult handleWebException(WebException ex) {
        log.error("WebExceptionHandler=>handleException=>[" + ex.getClass().getName() + ":" + ex.getMessage() + "]");
        WebResult webResult = ResponseResultConfig.getResponseResult(ResponseResultConfig.ResponseResult.EXCEPTION);
        webResult.setErrorMessage(ex.getMessage());
        return webResult;
    }

    /**
     * 统一处理不支持请求方法的异常，如该url只支持post请求而客户端调用使用了get请求就会抛出该异常
     *
     * @param ex
     * @return
     */
    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    @ResponseBody
    public WebResult handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
        log.error("WebExceptionHandler=>handleException=>[" + ex.getClass().getName() + ":" + ex.getMessage() + "]");
        WebResult webResult = ResponseResultConfig.getResponseResult(ResponseResultConfig.ResponseResult.EXCEPTION);
        webResult.setErrorMessage(ex.getMessage());
        return webResult;
    }

}
