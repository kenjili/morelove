package wujiuye.morelove;

import wujiuye.morelove.common.exception.WebResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.web.method.HandlerMethod;
import wujiuye.morelove.controller.UserController;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 使用内置tomcat方式启动
 * 由于spring boot是默认扫描source class所在包下的所有类和子包下的类的，所以不需要@Import其它配置类了
 * （即全盘扫描）
 * SpringApplication.run需要提供一个被@SpringBootApplication注释的类
 */
@SpringBootApplication
@Slf4j
public class MoreLoveApplication {

    public static void main(String[] argv) {
        log.debug("jar方式启动spring boot项目...");
        ApplicationContext applicationContext = SpringApplication.run(MoreLoveApplication.class, argv);
//        stuHandlerMethod(applicationContext);
    }

    private static void stuHandlerMethod(ApplicationContext applicationContext) {
        /**
         * 处理器方法
         * 知识点补充：
         *  HandlerMethod是springMVC的Controller某个url对应的某个Controller执行的目标方法。
         *  用于支持RequestMapping注解的
         *  HandlerMethod中存储了：
         *      Method（方法）所在的bean
         *      bean的类型
         *      该方法的参数，因为最终spring mvc调用该方法时需要根据参数类型传入参数才能调用
         */
        UserController indexController = applicationContext.getBean(UserController.class);
        String[] definitionNames = applicationContext.getBeanDefinitionNames();
        //所有所有控制器bean的名称
        for (String name :
                definitionNames) {
            if (name.indexOf("Controller") > 0) {
                System.out.print(name + " ==> 别名为：");
                String[] aliases = applicationContext.getAliases(name);//获取bean的别名
                for (String ali : aliases) {
                    System.out.println(ali);
                }
                System.out.println();
            }
        }
        try {
            //理解HandlerMethod的作用
            Method indexMethod = indexController.getClass()
                    //方法名，方法参数类型
                    .getMethod("doLogout", null/*String.class, Integer.class, Integer.class, Integer.class, Integer.class*/);
            HandlerMethod handlerMethod = new HandlerMethod(indexController, indexMethod);
            handlerMethod.getMethod().setAccessible(true);//设置方法为可访问如果是私有的方法
            if (indexMethod.getReturnType() == WebResult.class) {
                //调用目标bean的目标方法
                Object result = handlerMethod.getMethod().invoke(handlerMethod.getBean(), null/*"广州", null, null, null, null*/);
                System.out.println("result===>" + result);
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
