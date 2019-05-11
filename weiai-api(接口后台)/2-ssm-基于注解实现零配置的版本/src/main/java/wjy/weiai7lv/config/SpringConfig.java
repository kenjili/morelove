package wjy.weiai7lv.config;

import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

@Configuration
//配置包扫描,mvc的交给mvc配置
@ComponentScans(value = {
        @ComponentScan("wjy.weiai7lv.service.impl"),
        @ComponentScan("wjy.weiai7lv.aspect"),
        @ComponentScan("wjy.weiai7lv.tasks.impl"),
        @ComponentScan("wjy.weiai7lv.listener"),
        @ComponentScan("wjy.weiai7lv.shiro")
})
@Import(value = {
        RedisConfig.class,
        MyBatisConfig.class,
        ShiroConfig.class
})//其它@Configuration类
public class SpringConfig{

    /**
     * 要想使用@Value 用${}占位符注入属性，这个bean是必须的。
     * @return
     */
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

}
