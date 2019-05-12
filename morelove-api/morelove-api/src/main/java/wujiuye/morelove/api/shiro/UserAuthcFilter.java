package wujiuye.morelove.api.shiro;

import com.fasterxml.jackson.databind.ObjectMapper;
import wujiuye.morelove.common.exception.ResponseResultConfig;
import wujiuye.morelove.common.exception.WebResult;
import org.apache.shiro.web.filter.authc.UserFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * authc的过滤器
 */
public class UserAuthcFilter extends UserFilter {


    /**
     * 在访问被拒绝的时候调用,即用户未登录
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        //响应未登录
        WebResult webResult = ResponseResultConfig.getResponseResult(ResponseResultConfig.ResponseResult.NOTLOGIN);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        ObjectMapper mapper = new ObjectMapper();
        String resultJson = mapper.writeValueAsString(webResult);
        response.getWriter().write(resultJson);
        return false;
    }

}
