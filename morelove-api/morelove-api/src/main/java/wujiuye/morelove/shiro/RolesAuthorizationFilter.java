package wujiuye.morelove.shiro;

import com.fasterxml.jackson.databind.ObjectMapper;
import wujiuye.morelove.common.exception.ResponseResultConfig;
import wujiuye.morelove.common.exception.WebResult;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authz.AuthorizationFilter;


import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

/**
 * 自定义的基于角色的授权过滤器
 */
public class RolesAuthorizationFilter extends AuthorizationFilter {


    /**
     * 重写授权判断，判断用户是否有访问该资源的授权
     *
     * @param request
     * @param response
     * @param mappedValue
     * @return
     * @throws Exception
     */
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        Subject subject = getSubject(request, response);
        //未登录
        if (subject.getPrincipal() == null) {
            return false;
        }
        //该资源配置的允许访问的角色，如/user/edit=roles[lover,admin],
        //那么结果就是{"lover","admin"}
        if (mappedValue == null) return true;
        String[] roles = (String[]) mappedValue;
        if (roles.length == 0) {
            return true;
        }
        for (String role : roles) {
            //只要用户拥有其中任意的一个角色就能通过授权访问
            if (subject.hasRole(role)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 访问被拒绝时调用，即没有权限时被调用
     *
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws IOException {
        Subject subject = this.getSubject(request, response);
        WebResult webResult;
        if (subject.getPrincipal() == null)
            //响应未登录
            webResult = ResponseResultConfig.getResponseResult(ResponseResultConfig.ResponseResult.NOTLOGIN);
        else {
            //响应没有权限
            if(!subject.hasRole("lover")){
                //如果是没有lover角色
                webResult = ResponseResultConfig.getResponseResult(ResponseResultConfig.ResponseResult.NOTPERM_BINDLOVER);
            }else if(!subject.hasRole("admin")){
                //如果是没有admin角色
                webResult = ResponseResultConfig.getResponseResult(ResponseResultConfig.ResponseResult.NOTPERM_ADMIN);
            }else{
                webResult = ResponseResultConfig.getResponseResult(ResponseResultConfig.ResponseResult.NOTPERM);
            }
        }
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        ObjectMapper mapper = new ObjectMapper();
        String resultJson = mapper.writeValueAsString(webResult);
        response.getWriter().write(resultJson);
        return false;
    }
}
