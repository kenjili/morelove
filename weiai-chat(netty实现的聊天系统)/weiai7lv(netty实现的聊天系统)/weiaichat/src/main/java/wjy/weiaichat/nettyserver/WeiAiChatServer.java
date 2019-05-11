package wjy.weiaichat.nettyserver;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


/**
 * 服务端入口
 * @author wjy
 */
public class WeiAiChatServer {

    public static void main(String[] args) {
        //初始化spring
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
        applicationContext.getBean("serverBootstrap");
    }
}
