package com.wujiuye.weiai7lv.controller;


import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.wujiuye.weiai7lv.entity.SMS;
import com.wujiuye.weiai7lv.exception.ResponseResultConfig;
import com.wujiuye.weiai7lv.exception.WebResult;
import com.wujiuye.weiai7lv.tasks.SMSTaskService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Random;

@RequestMapping("sms")
@Controller
public class SMSController {

    @Autowired
    private SMSTaskService smsTaskService;

    /**
     * 发送短信验证码
     * @param request
     * @param response
     * @param phoneNumber
     * @return
     */
    @RequestMapping(value = "send",method = RequestMethod.POST)
    @ResponseBody
    public WebResult sendSMSVerificationCode(HttpServletRequest request, HttpServletResponse response,
                                             @RequestParam("phoneNumber")String phoneNumber){
        WebResult webResult;
        if(request.getSession().getAttribute("SMS")!=null){
            //1分钟只能发送一次
            SMS oldSms = (SMS) request.getSession().getAttribute("SMS");
            if(System.currentTimeMillis()/1000-oldSms.getSendDateTime()<=60){
                webResult = ResponseResultConfig.getResponseResult(ResponseResultConfig.ResponseResult.EXCEPTION);
                webResult.setErrorMessage("验证码发送频繁，请稍后再试！");
                return webResult;
            }
        }
        String code = getVerificationCode();
        SendSmsResponse sendSmsResponse = smsTaskService.sendSMSVerificationCode(phoneNumber,code);
        if(sendSmsResponse==null){
            webResult = ResponseResultConfig.getResponseResult(ResponseResultConfig.ResponseResult.EXCEPTION);
            webResult.setErrorMessage("短信验证码发送失败！");
            return webResult;
        }
        if(sendSmsResponse.getCode().equals("OK")){
            SMS sms = new SMS();
            sms.setPhoneNumber(phoneNumber);
            sms.setCode(code);
            //三分钟内有效
            sms.setOverdue(3*60);
            sms.setSendDateTime(System.currentTimeMillis()/1000);
            request.getSession().setAttribute("SMS",sms);
            webResult = ResponseResultConfig.getResponseResult(ResponseResultConfig.ResponseResult.SUCCESS);
            return webResult;
        }
        webResult = ResponseResultConfig.getResponseResult(ResponseResultConfig.ResponseResult.EXCEPTION);
        webResult.setErrorMessage(sendSmsResponse.getMessage());
        return webResult;
    }

    /**
     * 获取随机6位密码
     * @return
     */
    private String getVerificationCode(){
        String sources = "0123456789"; // 加上一些字母，就可以生成pc站的验证码了
        Random rand = new Random(System.currentTimeMillis());
        StringBuffer code = new StringBuffer();
        for (int j = 0; j < 6; j++)
        {
            code.append(sources.charAt(rand.nextInt(9)) + "");
        }
        return code.toString();
    }
}
