package wjy.weiai7lv.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import wjy.weiai7lv.tasks.EmailTaskService;
import wjy.weiai7lv.tasks.impl.EmailTaskServiceImpl;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/spring.xml")
public class SendEmailTest {

    @Autowired
    private EmailTaskService emailTaskService;

    @Test
    public void sendEmail(){

       // emailTaskService.sendEmailCode("15278712509@163.com","123456");
        new EmailTaskServiceImpl().sendEmailCode("15278712509@163.com","123456");
    }
}
