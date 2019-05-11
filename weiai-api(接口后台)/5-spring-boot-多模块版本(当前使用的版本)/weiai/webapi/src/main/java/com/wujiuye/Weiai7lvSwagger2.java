package com.wujiuye;

import io.swagger.annotations.ApiOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;

/**
 * swagger通过注解表明该接口会生成文档，包括接口名、请求方法、参数、返回信息的等等。
 *
 * @author wjy
 * <p>
 * 记得给"/swagger-ui.html"添加权限配置，不然只能登录才能查看
 * @Api：修饰整个类，描述Controller的作用
 * @ApiOperation：描述一个类的一个方法，或者说一个接口
 * @ApiParam：单个参数描述
 * @ApiModel：用对象来接收参数
 * @ApiProperty：用对象接收参数时，描述对象的一个字段
 * @ApiResponse：HTTP响应其中1个描述
 * @ApiResponses：HTTP响应整体描述
 * @ApiIgnore：使用该注解忽略这个API
 * @ApiError ：发生错误返回的信息
 * @ApiImplicitParam：一个请求参数
 * @ApiImplicitParams：多个请求参数
 */
@Configuration
@EnableSwagger2 //开启Swagger
public class Weiai7lvSwagger2 implements WebMvcConfigurer {


    /**
     * 因为 Swagger2的资源文件都是在jar包里面，如果不在此处配置加载静态资源，
     * 资源定位
     *
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
    }


    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)//文档类型，swagger2.0
                .apiInfo(new ApiInfoBuilder()
                        .title("微爱后端接口文档1.0版本")
                        .description("简单优雅的restfun风格")
                        //点击描述时跳转的链接
                        .termsOfServiceUrl("http://weiai.wujiuye.com/")
                        //文档的版本号
                        .version("1.0")
                        .build())
                .select()
                //配置了api文件也就是controller包的路径，不然生成的文档扫描不到接口。
                .apis(Weiai7lvSwagger2.basePackage(
                        "com.wujiuye.controller," +
                        "com.wujiuye.album.controller," +
                        "com.wujiuye.business.controller," +
                        "com.wujiuye.found.controller," +
                        "com.wujiuye.lovetime.controller"))
                .paths(PathSelectors.any())
                .build()
                .enable(true);
    }



    public static Predicate<RequestHandler> basePackage(final String basePackage) {
        return new Predicate<RequestHandler>() {
            @Override
            public boolean apply(RequestHandler input) {
                return declaringClass(input).transform(handlerPackage(basePackage)).or(true);
            }
        };
    }

    /**
     * 处理包路径配置规则,支持多路径扫描匹配以逗号隔开
     *
     * @param basePackage 扫描包路径
     * @return Function
     */
    private static Function<Class<?>, Boolean> handlerPackage(final String basePackage) {
        return new Function<Class<?>, Boolean>() {

            @Override
            public Boolean apply(Class<?> input) {
                for (String strPackage : basePackage.split(",")) {
                    boolean isMatch = input.getPackage().getName().startsWith(strPackage);
                    if (isMatch) {
                        return true;
                    }
                }
                return false;
            }
        };
    }

    /**
     * @param input RequestHandler
     * @return Optional
     */
    private static Optional<? extends Class<?>> declaringClass(RequestHandler input) {
        return Optional.fromNullable(input.declaringClass());
    }


}
