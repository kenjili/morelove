package wujiuye.morelove.controller;


import wujiuye.morelove.common.config.WebConstantConfig;
import wujiuye.morelove.common.exception.ResponseResultConfig;
import wujiuye.morelove.common.exception.WebResult;
import wujiuye.morelove.common.utils.StringUtils;
import wujiuye.morelove.pojo.User;
import wujiuye.morelove.pojo.SMS;
import wujiuye.morelove.service.UserService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserService userService;


    /**
     * 用户注册，调用之前请先调用发送验证码接口获取到验证码
     * 使用短信验证码注册
     *
     * @return
     */
    @RequestMapping(value = "regist", method = RequestMethod.POST)
    @ResponseBody
    public WebResult doRegist(
            HttpServletRequest request, HttpServletResponse response,
            //@RequestParam的required=false不强制客户端提交的表单一定含有该字段
            @RequestParam(value = "username", required = false) String username,
            @RequestParam(value = "password", required = false) String password,
            @RequestParam(value = "realname", required = false) String realname,
            @RequestParam(value = "phoneNumber", required = false) String phoneNumber,//手机号码
            @RequestParam(value = "smsCode", required = false) String smsCode,//短信验证码
            @RequestParam(value = "birthday", required = false) String birthday,
            @RequestParam(value = "sex", required = false) Integer sex
    ) throws Exception {
        //验证验证码
        if (request.getSession().getAttribute("SMS") == null) {
            WebResult webResult = ResponseResultConfig.getResponseResult(ResponseResultConfig.ResponseResult.EXCEPTION);
            webResult.setErrorMessage("请先获取验证码！");
            return webResult;
        }
        SMS sms = (SMS) request.getSession().getAttribute("SMS");
        request.getSession().removeAttribute("SMS");//验证码只能用一次
        if (System.currentTimeMillis() / 1000 - sms.getSendDateTime() > sms.getOverdue()) {
            //验证码过期
            WebResult webResult = ResponseResultConfig.getResponseResult(ResponseResultConfig.ResponseResult.EXCEPTION);
            webResult.setErrorMessage("验证码过期！");
            return webResult;
        }
        //手机号码不相同或短信验证码不同则失败
        if (!sms.getPhoneNumber().equals(phoneNumber)
                || !sms.getCode().equals(smsCode)) {
            WebResult webResult = ResponseResultConfig.getResponseResult(ResponseResultConfig.ResponseResult.EXCEPTION);
            webResult.setErrorMessage("验证码错误！");
            return webResult;
        }
        //注册
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setRealname(realname);
        user.setEmail(phoneNumber + "@163.com");//保留邮箱字段，默认使用手机号的163邮箱
        user.setPhoneNumber(phoneNumber);
        user.setDisable(false);//false不禁用,true禁用
        user.setBirthday(StringUtils.str2Date(birthday + " 01:01:00"));
        user.setSex((sex >= 0 && sex <= 1) ? sex : 0);
        WebResult webResult = ResponseResultConfig.getResponseResult(ResponseResultConfig.ResponseResult.SUCCESS);
        user = this.userService.registUser(user);
        //去掉密码再返回给客户端
        user.setPassword("");
        webResult.setData(user);
        return webResult;
    }

    /**
     * 用户登录
     *
     * @param username 用户名
     * @param password 密码
     * @return
     */
    @ApiOperation(value = "用户登录接口", notes = "调用用户登录接口需要提供用户名和密码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", dataTypeClass = String.class, required = true),
            @ApiImplicitParam(name = "password", value = "密码", dataTypeClass = String.class, required = true)
    })
    @RequestMapping(value = "login", method = RequestMethod.POST)
    @ResponseBody
    public WebResult doLogin(String username, String password) {
        //加密后的密码，想要认证成功，需要用户在注册的时候也使用同样的算法来加密密码
        String newPassword = new SimpleHash(WebConstantConfig.ALGORITH_NAME, password, ByteSource.Util.bytes(WebConstantConfig.SALT), WebConstantConfig.HASH_ITERATIONS).toHex();
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(username, newPassword);
        Subject subject = SecurityUtils.getSubject();
        try {
            //会执行配置的Realm的获取认证信息方法
            //如果Realm的doGetAuthenticationInfo方法返回的SimpleAuthenticationInfo对象的principal和credentials字段的值
            //与UsernamePasswordToken对象中的principal和credentials的值相等，则认证成功。
            subject.login(usernamePasswordToken);
            WebResult result = ResponseResultConfig.getResponseResult(ResponseResultConfig.ResponseResult.SUCCESS);
            return result;
        } catch (AuthenticationException e) {
            WebResult result = ResponseResultConfig.getResponseResult(ResponseResultConfig.ResponseResult.EXCEPTION);
            if (e instanceof UnknownAccountException) {
                //用户不存在
                result.setErrorMessage(e.getMessage());
            } else if (e instanceof IncorrectCredentialsException) {
                //密码错误
                result.setErrorMessage(e.getMessage());
            } else {
                //其它异常
                result.setErrorMessage(e.getMessage());
            }
            return result;
        }
    }

    /**
     * 用户退出登录
     *
     * @return
     */
    @RequestMapping(value = "logout", method = RequestMethod.GET)
    @ResponseBody
    public WebResult doLogout() {
        Subject subject = SecurityUtils.getSubject();
        User user = userService.getUserWithUsername(subject.getPrincipal().toString());
        subject.logout();
        userService.userLogout(user);
        WebResult webResult = ResponseResultConfig.getResponseResult(ResponseResultConfig.ResponseResult.SUCCESS);
        return webResult;
    }

}
