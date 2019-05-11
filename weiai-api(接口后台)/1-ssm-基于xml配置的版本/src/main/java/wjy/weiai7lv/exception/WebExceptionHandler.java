package wjy.weiai7lv.exception;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * 自定义全局异常处理器
 * 系统的dao、service、controller出现异常都是通过throws exception向上抛出的，
 * 最后spring mvc前端控制器将异常交由异常处理器进行处理。
 * 如果使用注解方法，则将异常交给@ControllerAdvice注解的类，查找@ExceptionHandler指定的异常类型相同的方法处理。
 */
@ControllerAdvice //不要忘记在<context:component-scan>标签中添加包扫描
public class WebExceptionHandler {

    private Logger logger = Logger.getLogger(this.getClass().getName());

    /**
     * 处理异常
     * @param request
     * @param ex
     * @return
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody //在实现的方法添加该注解将结果解析为json数据返回
    public WebResult handleException(HttpServletRequest request, Exception ex) {
        logger.error("WebExceptionHandler=>handleException=>["+ex.getClass().getName()+":"+ex.getMessage()+"]");
        if (ex instanceof WebException) {
            WebResult webResult = ResponseResultConfig.getResponseResult(ResponseResultConfig.ResponseResult.EXCEPTION);
            webResult.setErrorMessage(ex.getMessage());
            return webResult;
        } else {
            //开发阶段打印错误信息
            ex.printStackTrace();
            //错误信息不向用户展示
            WebResult webResult = ResponseResultConfig.getResponseResult(ResponseResultConfig.ResponseResult.EXCEPTION);
            webResult.setErrorMessage("系统出现异常！");
            return webResult;
        }
    }

}
