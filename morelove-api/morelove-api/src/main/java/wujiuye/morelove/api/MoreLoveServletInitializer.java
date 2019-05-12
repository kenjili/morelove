package wujiuye.morelove.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;


/**
 * 若打包成war包,则需要继承 org.springframework.boot.context.web.SpringBootServletInitializer类,
 * 覆盖其config(SpringApplicationBuilder)方法。
 * jar包和war包启动区别：
 * ​	jar包：执行SpringBootApplication的run方法，启动IOC容器，然后创建嵌入式的Servlet容器。
 * ​	war包：启动Servlet服务器，服务器启动SpringBoot应用【SpringBootServletInitializer】，然后才启动IOC容器。
 *
 * Servlet3.0+规则：
 *      服务器启动（web应用启动）会创建当前web应用里面每一个jar包里面ServletContainerInitializer实例；
 *
 * 知识点：
 * SpringBootServletInitializer是一个WebApplicationInitializer，
 * 前面ssm全注解配置使用的是AbstractAnnotationConfigDispatcherServletInitializer，
 * 需要修改servlet-api版本依赖为3.0以上
 * 在服务启动时会自动扫描项目包下的所有WebApplicationInitializer，自动调用onStartup方法，替代web.xml文件的配置
 * SpringBootServletInitializer重写了onStartup方法，而另外提供configure方法给我们做配置
 */
@Slf4j //lombok提供的注解，方便日记记录
public class MoreLoveServletInitializer extends SpringBootServletInitializer {

    /**
     * SpringBootServletInitializer重写了WebApplicationInitializer的onStartup方法，
     * 并在onStartup方法中完成spring容器的初始化，
     * 初始化流程：
     *      SpringBootServletInitializer::onStartup
     *          ->createRootApplicationContext
     *          ->run
     *   -> SpringApplication::run
     *          ->createApplicationContext
     * 所以说也还是调用SpringApplication的run方法来启动的
     * 最终容易的实例化是在SpringApplication的createApplicationContext方法中，默认是使用AnnotationConfigApplicationContext
     * @param application
     * @return
     */
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        log.debug("lombok的@Slf4j注解的使用","spring boot war包 项目启动...");
        /**
         * 需要指定源配置类，即我们可以重写创建一个类
         * 然后使用@SpringBootApplication注解，application.sources(被@SpringBootApplication注解的类);
         * 例如：
         *    application.sources(Weiai7lvApplicationConfig.class);
         * 只会处理sources类上面的注解和@Bean的方法
         */
        return application.sources(MoreLoveApplication.class);
    }


    /**
     * 重写onStartup方法代替web.xml做一些配置
     * @param servletContext
     * @throws ServletException
     */
    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
//        FilterRegistration.Dynamic  characterEncodingDynamic= servletContext.addFilter("characterEncodingFilter", CharacterEncodingFilter.class);
//        characterEncodingDynamic.addMappingForUrlPatterns(null,true,"/*");
//        characterEncodingDynamic.setInitParameter("encoding","UTF-8");

        //添加shiroFilter (注释掉， spring boot约定大于配置，不需要自己配置了)
//        FilterRegistration.Dynamic shiroFilterDynamic = servletContext.addFilter("shiroFilter", DelegatingFilterProxy.class);
//        shiroFilterDynamic.addMappingForUrlPatterns(null,true,"/*");

        //添加tomcat启动监听器
        //servletContext.addListener(InitRedisService.class);

        //必须调用父类的onStartup方法
        super.onStartup(servletContext);
    }
}
