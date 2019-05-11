package com.wujiuye.lovetime.service.impl;

import com.wujiuye.common.config.WebConstantConfig;
import com.wujiuye.common.utils.DateTimeUtils;
import com.wujiuye.common.utils.UploadImageFileUtils;
import com.wujiuye.dao.LoverDao;
import com.wujiuye.dao.UserDao;
import com.wujiuye.dao.LovetimeDao;
import com.wujiuye.lovetime.service.LovetimeService;
import com.wujiuye.pojo.Lover;
import com.wujiuye.pojo.User;
import com.wujiuye.pojo.Lovetime;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")//让idea对@Autowired等自动注入不警告
@Service
@Transactional
public class LovetimeServiceImpl implements LovetimeService {

    @Autowired
    private WebConstantConfig webConstantConfig;
    @Autowired
    private UserDao userDao;
    @Autowired
    private LoverDao loverDao;
    @Autowired
    private LovetimeDao lovetimeDao;

    /**
     * 发表恋爱时光
     *
     * @param context    内容
     * @param photoFiles 图片
     * @param isPublic   是否公开
     * @return
     */
    @Override
    public Lovetime publicLovetime(String context, MultipartFile[] photoFiles, boolean isPublic) {
        //获取通过shiro认证的用户信息
        String username = (String) SecurityUtils.getSubject().getPrincipal();
        User user = userDao.getUserWithUsername(username);
        Lover lover = loverDao.findLover(user.getId());
        //创建恋爱时光
        Lovetime lovetime = new Lovetime();
        lovetime.setDetails(context);
        lovetime.setUser(user);
        lovetime.setState(isPublic ? 1 : 0);
        String rootPath = isPublic ? webConstantConfig.getPublicUploadFileRootPath() :
                webConstantConfig.getPrivateUploadFileRootPath();
        //恋爱时光存储照片的相对路径==>情侣id/用户id/年月日
        String photoPath = "/lover-" + lover.getId() + "/user-" + user.getId() + "/" + DateTimeUtils.getCurrentYMDString();
        if (photoFiles.length >= 1) {
            //获取图片保存的相对路径【原图和缩略图】，=情侣相册路径/图片id
            String[] relativePathAndthumbImagePath = UploadImageFileUtils.savaImageFile(photoFiles[0], rootPath, webConstantConfig.getLovetimeImagesRootPath() + photoPath);
            //更新图片路径信息
            lovetime.setImg1(relativePathAndthumbImagePath[0]);
            lovetime.setImgThumb1(relativePathAndthumbImagePath[1]);
        }
        if (photoFiles.length >= 2) {
            //获取图片保存的相对路径【原图和缩略图】，=情侣相册路径/图片id
            String[] relativePathAndthumbImagePath = UploadImageFileUtils.savaImageFile(photoFiles[1], rootPath, webConstantConfig.getLovetimeImagesRootPath() + photoPath);
            //更新图片路径信息
            lovetime.setImg2(relativePathAndthumbImagePath[0]);
            lovetime.setImgThumb2(relativePathAndthumbImagePath[1]);
        }
        if (photoFiles.length >= 3) {
            //获取图片保存的相对路径【原图和缩略图】，=情侣相册路径/图片id
            String[] relativePathAndthumbImagePath = UploadImageFileUtils.savaImageFile(photoFiles[2], rootPath, webConstantConfig.getLovetimeImagesRootPath() + photoPath);
            //更新图片路径信息
            lovetime.setImg3(relativePathAndthumbImagePath[0]);
            lovetime.setImgThumb3(relativePathAndthumbImagePath[1]);
        }

        int rows = lovetimeDao.savaLovetime(lovetime);
        if (rows == 1) {
            lovetime = lovetimeDao.getLovetime(lovetime.getId());
            pastLovetimeImgUrl(lovetime);
        }
        return lovetime;
    }

    @Override
    public List<Lovetime> queryLovetime(int pageSize, int page) {
        if (page < 1) page = 1;
        if (pageSize < 5) pageSize = 5;
        int start = (page - 1) * pageSize;
        //获取通过shiro认证的用户信息
        String username = (String) SecurityUtils.getSubject().getPrincipal();
        User user = userDao.getUserWithUsername(username);
        Lover lover = loverDao.findLover(user.getId());
        //查询包括公开的
        List<Lovetime> lovetimeList = lovetimeDao.queryLovetime(0, start, pageSize, lover);
        for (Lovetime lovetime : lovetimeList) {
            pastLovetimeImgUrl(lovetime);
        }
        return lovetimeList;
    }

    @Override
    public List<Lovetime> queryPublicLovetime(int pageSize, int page) {
        if (page < 1) page = 1;
        if (pageSize < 5) pageSize = 5;
        int start = (page - 1) * pageSize;
        List<Lovetime> lovetimeList = lovetimeDao.queryPublicLovetime(start, pageSize);
        for (Lovetime lovetime : lovetimeList) {
            pastLovetimeImgUrl(lovetime);
        }
        return lovetimeList;
    }


    /**
     * 获取详情时更新访问数量
     *
     * @param isPublic   是否只能获取公开的
     * @param lovetimeId
     * @return
     */
    @Override
    public Lovetime lovetimeDetails(boolean isPublic, int lovetimeId) {
        Lovetime lovetime = lovetimeDao.getLovetime(lovetimeId);
        boolean isFlag = false;
        if (lovetime != null) {
            //获取通过shiro认证的用户信息
            String username = (String) SecurityUtils.getSubject().getPrincipal();
            User user = userDao.getUserWithUsername(username);
            if (lovetime.getUser().getId() == user.getId())
                //当前用户自己发的
                isFlag = true;
            else if (isPublic && lovetime.getState() == 1) {
                //只能获取公开的，且这条记录是公开的
                isFlag = true;
            } else {
                //情侣之间可以查询私密状态的
                Lover lover = loverDao.findLover(user.getId());
                if (lover != null && (lovetime.getUser().getId() == lover.getMan().getId()
                        || lovetime.getUser().getId() == lover.getWomen().getId()))
                    isFlag = true;
            }
        }
        if (isFlag) {
            //更新访问数量
            lovetimeDao.updateLovetimeAccess(lovetimeId);
            //解析图片链接
            pastLovetimeImgUrl(lovetime);
        }
        return lovetime;
    }


    /**
     * 为图片路径添加域名+根路径
     *
     * @param lovetime
     */
    private void pastLovetimeImgUrl(Lovetime lovetime) {
        if (lovetime == null) return;
        String serverName = lovetime.getState() == 1 ? webConstantConfig.getPublicResourceServerName() :
                webConstantConfig.getPrivateResourceServerName();

        if (lovetime.getImg1() != null) {
            lovetime.setImg1(serverName + lovetime.getImg1());
        }
        if (lovetime.getImg2() != null) {
            lovetime.setImg2(serverName + lovetime.getImg2());
        }
        if (lovetime.getImg3() != null) {
            lovetime.setImg3(serverName + lovetime.getImg3());
        }
        if (lovetime.getImgThumb1() != null) {
            lovetime.setImgThumb1(serverName + lovetime.getImgThumb1());
        }
        if (lovetime.getImgThumb2() != null) {
            lovetime.setImgThumb2(serverName + lovetime.getImgThumb2());
        }
        if (lovetime.getImgThumb3() != null) {
            lovetime.setImgThumb3(serverName + lovetime.getImgThumb3());
        }
    }
}
