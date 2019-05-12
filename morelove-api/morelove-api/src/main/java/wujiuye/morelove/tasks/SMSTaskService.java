package wujiuye.morelove.tasks;

import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;

public interface SMSTaskService {

    /**
     * 发送短信验证码
     * @param targetPhoneNumber 接收方的手机号码
     * @param verificationCode  短信验证码
     * @return  true为发送成功，false发送失败
     */
    SendSmsResponse sendSMSVerificationCode(String targetPhoneNumber, String verificationCode);

}
