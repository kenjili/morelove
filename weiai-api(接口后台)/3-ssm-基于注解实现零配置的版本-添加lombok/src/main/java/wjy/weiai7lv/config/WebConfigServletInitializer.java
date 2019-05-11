package wjy.weiai7lv.config;

import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;
import wjy.weiai7lv.listener.InitRedisService;

import javax.servlet.*;

/**
 * 需要修改servlet-api版本依赖为3.0以上
 * 参考文献：
 * Spring 4 学习笔记7：MVC 配置（JAVA方式）：https://blog.csdn.net/w1196726224/article/details/52687324
 * Spring MVC 无XML配置入门示例：https://blog.csdn.net/classicer/article/details/50753019
 * SpringMvc+Spring+MyBatis 基于注解整合：https://www.cnblogs.com/niechen/p/springmvc.html#_label2
 * spring4.0 @PropertySource读取配置文件：https://blog.csdn.net/zz210891470/article/details/68948723
 *
 */
public class WebConfigServletInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {
    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[]{SpringConfig.class};
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[]{SpringMVCConfig.class};
    }

    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }


    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        super.onStartup(servletContext);

        FilterRegistration.Dynamic  characterEncodingDynamic= servletContext.addFilter("characterEncodingFilter", CharacterEncodingFilter.class);
        characterEncodingDynamic.addMappingForUrlPatterns(null,true,"/*");
        characterEncodingDynamic.setInitParameter("encoding","UTF-8");

        FilterRegistration.Dynamic shiroFilterDynamic = servletContext.addFilter("shiroFilter",DelegatingFilterProxy.class);
        shiroFilterDynamic.addMappingForUrlPatterns(null,true,"/*");

        servletContext.addListener(InitRedisService.class);
    }
}
