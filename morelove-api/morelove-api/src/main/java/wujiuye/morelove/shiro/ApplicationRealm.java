package wujiuye.morelove.shiro;

import wujiuye.morelove.common.utils.CusAccessUtils;
import wujiuye.morelove.pojo.User;
import wujiuye.morelove.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 如果不需要验证授权，则可以实现Realm接口即可
 */
@Component
@Slf4j
public class ApplicationRealm extends AuthorizingRealm {

    @Autowired
    private UserService userService;

    public String getName() {
        return "WeiAi7LvRealm";
    }

    /**
     * 该Realm是否支持验证给定的AuthenticationToken
     *
     * @param authenticationToken
     * @return
     */
    public boolean supports(AuthenticationToken authenticationToken) {
        return authenticationToken instanceof UsernamePasswordToken;
    }

    /**
     * 获取身份验证信息
     * 认证
     *
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        log.info("执行认证身份信息方法===>doGetAuthenticationInfo");
        UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) authenticationToken;
        User user = userService.getUserWithUsername(usernamePasswordToken.getUsername());
        if (user == null) {
            throw new UnknownAccountException("用户不存在！");
        }
        if (user.isDisable()) {
            throw new AuthenticationException("账号未激活或已经被禁用！");
        }
        //获取登录设备信息
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String lastLoginDevice = CusAccessUtils.getUserAgent(request) + "[" + CusAccessUtils.getIpAddress(request) + "]";
        //获取用户登录状态信息
        Map<String, Object> userLoginInfo = userService.queryUserLoginInfo(user);
        if (userLoginInfo == null) {
            userService.addUserLoginInfo(user);
            userService.updateUserLoginInfo(user, lastLoginDevice, 0);
            userLoginInfo = userService.queryUserLoginInfo(user);
        }
        //判断用户登录功能是否被禁用,如果是禁用状态判断当前时间是否允许登录时间之后
        if (userLoginInfo.get("disableState").equals(true)
                && ((Date) userLoginInfo.get("disableDatetime")).getTime() > System.currentTimeMillis()) {
            Long time = ((Date) userLoginInfo.get("disableDatetime")).getTime() - System.currentTimeMillis();
            time /= (1000 * 60);
            if (time < 1) time = 1l;
            throw new IncorrectCredentialsException("密码输入错误次数过多，请" + time + "分钟后再尝试！");
        }
        //验证用户信息是否正确
        if (!user.getUsername().equals(usernamePasswordToken.getUsername())
                || !user.getPassword().equals(String.valueOf(usernamePasswordToken.getPassword()))) {
            //密码错误次数加一，如果密码错误30分钟后重试
            userService.updateUserLoginInfo(user, lastLoginDevice, ((Integer) userLoginInfo.get("passwordErrorCount")) + 1);
            throw new IncorrectCredentialsException("用户名或密码错误！");
        }
        //认证成功清除用户登录状态信息标志
        userService.updateUserLoginInfo(user, lastLoginDevice, 0);
        //验证成功返回SimpleAuthenticationInfo对象
        //SimpleAuthenticationInfo的principal和credentials字段的值必须与UsernamePasswordToken对象中的principal和credentials的值相等，否则认证失败。
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(user.getUsername(), user.getPassword(), this.getName());
        return authenticationInfo;
    }


    /**
     * 获取授权信息
     * 授权
     *
     * @param principalCollection
     * @return
     */
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        log.info("执行获取授权信息的方法===>doGetAuthorizationInfo");
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        //获取认证的用户
        Subject subject = SecurityUtils.getSubject();
        String username = (String) subject.getPrincipal();
        //获取认证的用户所拥有的角色
        List<String> roleList = userService.rolesListWithUser(username);
        authorizationInfo.addRoles(roleList);
        return authorizationInfo;
    }
}
