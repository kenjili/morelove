package wujiuye.morelove.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;


/**
 * 在spring上下文启动完成之后清除redis的缓存
 *
 * @author wjy
 */
@Slf4j
@Component
public class InitRedisSpringApplicationEvent implements ApplicationListener<ApplicationReadyEvent> {

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
//        ApplicationContext applicationContext = applicationReadyEvent.getApplicationContext();
//        log.info("=======>初始化redis,清空缓存.....");
//        //Jedis连接工厂
//        JedisConnectionFactory jedisConnectionFactory = (JedisConnectionFactory) applicationContext.getBean("jedisConnectionFactory");
//        //获取一个连接并执行flushDb操作, Flushdb 命令用于清空当前数据库中的所有 key
//        jedisConnectionFactory.getConnection().flushDb();

    }

}
