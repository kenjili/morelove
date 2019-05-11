package wjy.weiai7lv.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * web项目的常量配置
 *
 * @author wjy
 */
@Configuration //被@Configuration注解的配置类也是一个bean
@PropertySource("classpath:webConfig.properties")
public class WebConstantConfig {

    //干扰数据防破解
    public final static String SALT = "wujiuye%#$@";
    //散列算法类型为MD5
    public final static String ALGORITH_NAME = "md5";
    //hash的次数
    public final static int HASH_ITERATIONS = 2;

    @Value("${RootPath.UploadFile.Private}")
    private String privateUploadFileRootPath;
    @Value("${RootPath.UploadFile.Public}")
    private String publicUploadFileRootPath;
    @Value("${ServerName.Resource.Private}")
    private String privateResourceServerName;
    @Value("${ServerName.Resource.Public}")
    private String publicResourceServerName;
    @Value("${ImagesRootPath.Album}")
    private String albumRootPath;
    @Value("${ImagesRootPath.Lovetime}")
    private String lovetimeImagesRootPath;
    @Value("${ImagesRootPath.Memorialday}")
    private String memorialDayImagesRootPath;

    public String getPrivateUploadFileRootPath() {
        return privateUploadFileRootPath;
    }

    public String getPublicUploadFileRootPath() {
        return publicUploadFileRootPath;
    }

    public String getPrivateResourceServerName() {
        return privateResourceServerName;
    }

    public String getPublicResourceServerName() {
        return publicResourceServerName;
    }

    public String getLovetimeImagesRootPath() {
        return lovetimeImagesRootPath;
    }

    public String getAlbumRootPath() {
        return albumRootPath;
    }

    public String getMemorialDayImagesRootPath() {
        return memorialDayImagesRootPath;
    }


}
