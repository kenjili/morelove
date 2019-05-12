package wujiuye.morelove.api.tasks.impl;

import wujiuye.morelove.api.tasks.EmailTaskService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;


/**
 * 邮件助手
 */
@Component
public class EmailTaskServiceImpl implements EmailTaskService {

    //163邮箱
    private final String emailUsername = "betcode@163.com";
    // 授权码
    private final String emailPassword = "weiai7lv5201314";
    //端口
    private final int smtpPort = 465;

    private Properties getProperties() {
        Properties props = new Properties();
        props.setProperty("mail.smtp.host", "smtp.163.com");
        props.setProperty("mail.smtp.auth", "true");
        props.setProperty("mail.smtp.port", String.valueOf(smtpPort));
        props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        return props;
    }

    /**
     * 发送验证码
     * 被@Async注解的方法交由线程池执行
     */
    //@Async(value = "asyncExecutor") 可指定使用哪一个线程池
    @Async
    @Override
    public void sendEmailCode(String email, String code) {
        Session session = Session.getDefaultInstance(getProperties(), new Authenticator() {
            @Override
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(emailUsername, emailPassword);
            }
        });

        Message message = new MimeMessage(session);
        try {
            // 设置发件人：
            message.setFrom(new InternetAddress(emailUsername));
            // 设置收件人:
            message.addRecipient(MimeMessage.RecipientType.TO, new InternetAddress(email));
            // 设置主题:
            message.setSubject("验证码");
            // 设置内容：
            message.setContent("【微爱】尊敬的用户，您本次验证码为：" + code + "，如非本人操作，请忽略！", "text/html;charset=UTF-8");
            Transport.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送激活链接
     * 被@Async注解的方法交由线程池执行
     * 打印结果：当前线程名称:threadPoolTaskExecutor-1
     *
     * @param email
     * @param link
     */
    @Async
    @Override
    public void sendActivationLink(String email, String link) {
        Session session = Session.getDefaultInstance(getProperties(), new Authenticator() {
            @Override
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(emailUsername, emailPassword);
            }
        });
        Message message = new MimeMessage(session);
        try {
            // 设置发件人：
            message.setFrom(new InternetAddress(emailUsername));
            // 设置收件人:
            message.addRecipient(MimeMessage.RecipientType.TO, new InternetAddress(email));
            // 设置主题:
            message.setSubject("【微爱】账号激活");
            // 设置内容：
            message.setContent("【微爱】感谢您使用微爱，您的账号已注册成功，点击链接即可激活：<a href=\"" + link + "\">" + link + "</a>", "text/html;charset=UTF-8");
            Transport.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
