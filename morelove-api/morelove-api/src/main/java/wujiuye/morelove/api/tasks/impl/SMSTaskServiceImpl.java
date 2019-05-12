package wujiuye.morelove.api.tasks.impl;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import wujiuye.morelove.common.utils.StringUtils;
import wujiuye.morelove.api.tasks.SMSTaskService;
import org.springframework.stereotype.Service;

/**
 * 短信服务，使用阿里云短信服务
 * 阿里云短信服务文档链接：https://help.aliyun.com/document_detail/55284.html?spm=a2c4g.11174283.3.1.8d482c42qjsri1#h2--sendsms-1
 * @author wjy
 */
@Service
//@Component
public class SMSTaskServiceImpl implements SMSTaskService {

    //连接超时
    private static final long CONNECT_TIMEOUT = 5*1000;
    //读取超时
    private static final long READ_TIMEOUT = 5*1000;

    //初始化ascClient需要的几个参数
    private static final String product = "Dysmsapi";//短信API产品名称（短信产品名固定，无需修改）
    private static final String domain = "dysmsapi.aliyuncs.com";//短信API产品域名（接口地址固定，无需修改）

    //密码对
    private static final String accessKeyId = "LTAIGANpFH6Ztos0";
    private static final String accessKeySecret = "dQDzuhLgIxoiYa4nijMbqyPfuqYa5A";

    //短信签名,设置链接：https://dysms.console.aliyun.com/dysms.htm?spm=a2c4g.11186623.2.12.488814d1LYalhM#/domestic/text/sign
    private static final String signName = "全栈攻城狮之道";
    //短信模版
    private static final String templateCode = "SMS_148593358";

    /**
     * 发送短信验证码，由于需要获取返回值，所以取消异常任务
     * @param targetPhoneNumber 接收方的手机号码
     * @param verificationCode  短信验证码
     * @return
     */
//    @Async
    @Override
    public SendSmsResponse sendSMSVerificationCode(String targetPhoneNumber, String verificationCode) {
        if(StringUtils.strIsNull(targetPhoneNumber)){
            //最好设置匹配密码
            return null;
        }
        //设置超时时间
        System.setProperty("sun.net.client.defaultConnectTimeout", String.valueOf(CONNECT_TIMEOUT));
        System.setProperty("sun.net.client.defaultReadTimeout", String.valueOf(READ_TIMEOUT));
        //初始化ascClient,暂时不支持多region（请勿修改）
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId,
                accessKeySecret);
        DefaultProfile.addEndpoint("cn-hangzhou", product, domain);
        IAcsClient acsClient = new DefaultAcsClient(profile);
        //组装请求对象
        SendSmsRequest request = new SendSmsRequest();
        //使用post提交
        request.setMethod(MethodType.POST);
        //必填:待发送手机号。支持以逗号分隔的形式进行批量调用，批量上限为1000个手机号码,批量调用相对于单条调用及时性稍有延迟,验证码类型的短信推荐使用单条调用的方式；发送国际/港澳台消息时，接收号码格式为国际区号+号码，如“85200000000”
        request.setPhoneNumbers(targetPhoneNumber);
        //必填:短信签名-可在短信控制台中找到
        request.setSignName(signName);
        //必填:短信模板-可在短信控制台中找到，发送国际/港澳台消息时，请使用国际/港澳台短信模版
        request.setTemplateCode(templateCode);
        //可选:模板中的变量替换JSON串,如模板内容为"您的验证码为${code}"时,此处的值为
        request.setTemplateParam("{\"code\":\""+verificationCode+"\"}");
        //请求失败这里会抛ClientException异常
        SendSmsResponse sendSmsResponse;
        try {
            //执行请求获取请求结果
            sendSmsResponse = acsClient.getAcsResponse(request);
            return sendSmsResponse;
        } catch (ClientException e) {
            e.printStackTrace();
            return null;
        }
    }
}
