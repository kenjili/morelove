package wjy.weiai7lv.controller;


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
import org.springframework.web.bind.annotation.*;
import wjy.weiai7lv.entity.SMS;
import wjy.weiai7lv.entity.User;
import wjy.weiai7lv.exception.ResponseResultConfig;
import wjy.weiai7lv.exception.WebResult;
import wjy.weiai7lv.service.UserService;
import wjy.weiai7lv.utils.StringUtils;
import wjy.weiai7lv.config.WebConfig;

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
    @RequestMapping(value = "login", method = RequestMethod.POST)
    @ResponseBody
    public WebResult doLogin(String username, String password) {
        //加密后的密码，想要认证成功，需要用户在注册的时候也使用同样的算法来加密密码
        String newPassword = new SimpleHash(WebConfig.ALGORITH_NAME, password, ByteSource.Util.bytes(WebConfig.SALT), WebConfig.HASH_ITERATIONS).toHex();
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
     * @return
     */
    @RequestMapping(value = "logout",method = RequestMethod.GET)
    @ResponseBody
    public WebResult doLogout(){
        Subject subject = SecurityUtils.getSubject();
        User user= userService.getUserWithUsername(subject.getPrincipal().toString());
        subject.logout();
        userService.userLogout(user);
        WebResult webResult = ResponseResultConfig.getResponseResult(ResponseResultConfig.ResponseResult.SUCCESS);
        return webResult;
    }

}
