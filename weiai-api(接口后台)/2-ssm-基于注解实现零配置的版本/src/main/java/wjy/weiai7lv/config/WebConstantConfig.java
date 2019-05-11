package wjy.weiai7lv.config;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * web项目的常量配置
 *
 * @author wjy
 */
public class WebConstantConfig {

    //干扰数据防破解
    public final static String SALT = "wujiuye%#$@";
    //散列算法类型为MD5
    public final static String ALGORITH_NAME = "md5";
    //hash的次数
    public final static int HASH_ITERATIONS = 2;


    /**
     * 获取域名
     *
     * @param request
     * @return
     */
    public static String getDomain(HttpServletRequest request) {
        StringBuffer url = request.getRequestURL();
        String tempContextUrl = url.delete(url.length() - request.getRequestURI().length(), url.length()).append("/").toString();
        return tempContextUrl;
    }

    private static Properties properties = new Properties();

    /**
     * 读取配置文件
     */
    static {
        InputStream in = WebConstantConfig.class.getClassLoader().getResourceAsStream("webConfig.properties");
        try {
            properties.load(in);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取资源所在服务器的域名
     *
     * @return
     */
    private static String getResourceServerName() {
        return properties.getProperty("resourceServerName");
    }

    /**
     * 获取上传文件保存的根路径
     *
     * @return
     */
    private static String getFileBasePath() {
        //获取key对应的value值
        return properties.getProperty("uploadFileRootPath");
    }

    /**
     * 获取旅行记上传相册所存放的根路径
     *
     * @return
     */
    public static String getAlbumRootPath() {
        return getFileBasePath() + properties.getProperty("albumRootPath");
    }


    /**
     * 获取发表恋爱时光时上传图片文件所存放的根路径
     *
     * @return
     */
    public static String getLovetimeImagesRootPath() {
        return getFileBasePath() + properties.getProperty("lovetimeImagesRootPath");
    }

    /**
     * 获取相册图片返回给客户端的url拼接的域名
     *
     * @return
     */
    public static String getAlbumResourceServerName() {
        return getResourceServerName() + properties.getProperty("albumRootPath");
    }


    /**
     * 获取恋爱时光图片返回给客户端的url拼接的域名
     *
     * @return
     */
    public static String getLovetimeResourceServerName() {
        return getResourceServerName() + properties.getProperty("lovetimeImagesRootPath");
    }

    /**
     * 获取纪念日背景图片保存的根路径
     *
     * @return
     */
    public static String getMemorialDayImageRootPath() {
        return getFileBasePath() + properties.getProperty("memorialDayImagesRootPath");
    }

    /**
     * 获取纪念日图片返回给客户端的url拼接的域名
     * @return
     */
    public static String getMemorialDayResourceServerName() {
        return getResourceServerName() + properties.getProperty("memorialDayImagesRootPath");
    }
}
