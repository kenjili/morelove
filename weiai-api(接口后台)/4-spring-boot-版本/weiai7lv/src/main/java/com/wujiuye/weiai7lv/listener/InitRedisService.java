package com.wujiuye.weiai7lv.listener;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * 在服务启动时清除redis的缓存
 *
 * @author wjy
 */
//@Component
public class InitRedisService implements ServletContextListener, ApplicationContextAware {

    //声明@Component是为了让spring初始化一个InitRedisService实例，这样就可以通过
    //实现ApplicationContextAware接口获取到ApplicationContext对象。
    //由于web.xml配置的listener都是tomcat创建的，所以在spring初始化完成之后InitRedisService的静态字段applicationContext是被赋值了的
    private static ApplicationContext applicationContext;

    /**
     * tomcat启动时调用
     *
     * @param servletContextEvent
     */
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        System.out.println("=======>初始化redis,清空缓存.....");
        //Jedis连接工厂
        JedisConnectionFactory jedisConnectionFactory = (JedisConnectionFactory) applicationContext.getBean("jedisConnectionFactory");
        //获取一个连接并执行flushDb操作, Flushdb 命令用于清空当前数据库中的所有 key
        jedisConnectionFactory.getConnection().flushDb();
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        InitRedisService.applicationContext = applicationContext;
    }
}
