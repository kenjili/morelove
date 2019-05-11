package wjy.weiai7lv.test;


import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import wjy.weiai7lv.tasks.SMSTaskService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/spring.xml")
public class SendSMSTest {

    @Autowired
    private SMSTaskService smsTaskService;

    @Test
    public void testSendSMS() {
        SendSmsResponse smsResponse = smsTaskService.sendSMSVerificationCode("15278712509", "123456");
        if(smsResponse==null)
            return;
        if (smsResponse.getCode() != null && smsResponse.getCode().equals("OK")) {
            //请求成功
            System.out.println("发送成功！");
        }else{
            System.out.println(smsResponse.getMessage());
        }
    }

}
