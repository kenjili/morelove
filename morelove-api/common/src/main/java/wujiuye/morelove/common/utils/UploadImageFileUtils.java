package wujiuye.morelove.common.utils;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * 图片文件上传工具
 *
 * @author wjy
 */
public class UploadImageFileUtils {

    /**
     * 保存图片文件到指定上传目录
     *
     * @param imgFile  图片文件，仅支持一张一张存储
     * @param rootPath 保存的根目录
     * @param savaPath 相对路径，如："/lovetime/情侣id/相册id／图片id"
     * @return 下标0是原图url，下标1是缩略图url
     */
    public static String[] savaImageFile(MultipartFile imgFile, String rootPath, String savaPath) {
        //获取保存到服务器上的永不重复的文件名
        String realFileName = getRandomFileName();
        //获取扩展名
        String extension = getFileExtension(imgFile);
        //创建目录
        File dirPath = new File(rootPath + savaPath);
        if (!dirPath.exists()) {
            dirPath.mkdirs();
        }

        //保存缩略图
        String thumbImagePath = savaPath + File.separatorChar + "thumb_" + realFileName + extension;
        String thumbAbsolutePath = rootPath + File.separatorChar + thumbImagePath;
        savaImageThumb(imgFile, thumbAbsolutePath);
        updatePath777(thumbAbsolutePath);

        //保存原图
        //文件的最终相对路径
        String relativePath = savaPath + File.separatorChar + realFileName + extension;
        //文件的绝对路径
        String absolutePath = rootPath + relativePath;
        //创建目标文件并保存
        File dest = new File(absolutePath);
        try {
            //将CommonsMultipartFile数据存放到目标文件
            imgFile.transferTo(dest);
        } catch (IOException e) {
            e.printStackTrace();
        }
        updatePath777(absolutePath);
        return new String[]{relativePath, thumbImagePath};
    }


    /**
     * 修改指定路径为所有用户可读可写可执行权限
     *
     * @param path
     */
    private static void updatePath777(String path) {
        try {
            //修改目录的访问权限
            Runtime.getRuntime().exec("chmod -R 777 " + path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 保存缩略图，返回缩略图的相对路径
     * 缩略图添加水印
     *
     * @return
     */
    private static void savaImageThumb(MultipartFile imgFile, String thumbAbsolutePath) {
        try {
            Thumbnails.of(imgFile.getInputStream())
                    .scale(0.5)//宽高缩放比例
                    .outputQuality(0.25)//质量缩放
                    .watermark(
                            //位置
                            Positions.BOTTOM_RIGHT,
                            //水印图片
                            ImageIO.read(new File(Thread.currentThread().getContextClassLoader().getResource("/").getPath() + "images/morelove-logo-shuiying.png")),
                            //透明度
                            0.6f)
                    .toFile(thumbAbsolutePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 获取文件扩展名
     *
     * @param imgFile
     * @return
     */
    private static String getFileExtension(MultipartFile imgFile) {
        String fileName = imgFile.getOriginalFilename();
        return fileName.substring(fileName.lastIndexOf("."));
    }

    /**
     * 获取随机不重复的图片文件名
     *
     * @return
     */
    private static String getRandomFileName() {
        Random random = new Random(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd-HHmmss");
        return sdf.format(new Date(System.currentTimeMillis())) + "-" + random.nextInt(8999) + 1000;
    }

}
