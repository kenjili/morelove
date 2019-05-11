package wjy.weiai7lv.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.util.ArrayList;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Configuration
//包扫描
@ComponentScans(
        value = {
                @ComponentScan("wjy.weiai7lv.controller"),
                @ComponentScan("wjy.weiai7lv.exception")
        }
)
@EnableWebMvc//将此注释添加到@Configuration类将从WebMvcConfigurationSupport导入Spring MVC配置。
public class SpringMVCConfig extends WebMvcConfigurerAdapter {


        @Override
        public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
                //这样可以使用DefaultServlet对静态资源进行处理，省去配置资源路径的任务
                configurer.enable();
        }


        /**
         * 添加消息转换器
         * @param converters
         */
        @Override
        public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
                MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();
                List<MediaType> mediaTypeList = new ArrayList<>();
                mediaTypeList.add(MediaType.parseMediaType("text/json;charset=UTF-8"));
                mediaTypeList.add(MediaType.parseMediaType("application/json;charset=UTF-8"));
                //支持的媒体类型
                messageConverter.setSupportedMediaTypes(mediaTypeList);
                ObjectMapper objectMapper = new ObjectMapper();
                //设置不输出值为null的字段
                objectMapper.setSerializationInclusion(NON_NULL);
                messageConverter.setObjectMapper(objectMapper);
                converters.add(messageConverter);
        }

        /**
         * 配置视图解析器
         * @param registry
         */
        @Override
        public void configureViewResolvers(ViewResolverRegistry registry) {
                InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
                viewResolver.setPrefix("/WEB-INF/pages/");
                viewResolver.setSuffix(".jsp");
                registry.viewResolver(viewResolver);
        }

        /**
         *
         * @return
         */
        @Bean
        public CommonsMultipartResolver multipartResolver(){
                CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
                multipartResolver.setDefaultEncoding("utf-8");
                multipartResolver.setMaxUploadSize(1024*1024*100);
                return multipartResolver;
        }

}
