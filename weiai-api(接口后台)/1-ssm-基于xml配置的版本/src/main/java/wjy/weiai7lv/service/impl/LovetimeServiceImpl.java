package wjy.weiai7lv.service.impl;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import wjy.weiai7lv.config.WebConfig;
import wjy.weiai7lv.dao.LoverDao;
import wjy.weiai7lv.dao.LovetimeDao;
import wjy.weiai7lv.dao.UserDao;
import wjy.weiai7lv.entity.Lover;
import wjy.weiai7lv.entity.Lovetime;
import wjy.weiai7lv.entity.User;
import wjy.weiai7lv.service.LovetimeService;
import wjy.weiai7lv.utils.DateTimeUtils;
import wjy.weiai7lv.utils.UploadImageFileUtils;

import java.util.List;


@Service
@Transactional
public class LovetimeServiceImpl implements LovetimeService {

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

        //恋爱时光存储照片的相对路径==>情侣id/用户id/年月日
        String photoPath = "/lover_" + lover.getId() + "/user_" + user.getId() + "/" + DateTimeUtils.getCurrentYMDString();
        if (photoFiles.length >= 1) {
            //获取图片保存的相对路径【原图和缩略图】，=情侣相册路径/图片id
            String[] relativePathAndthumbImagePath = UploadImageFileUtils.savaImageFile(photoFiles[0], photoPath, UploadImageFileUtils.UploadImageType.LOVETIME_IMAGES);
            //更新图片路径信息
            lovetime.setImg1(relativePathAndthumbImagePath[0]);
            lovetime.setImgThumb1(relativePathAndthumbImagePath[1]);
        }
        if (photoFiles.length >= 2) {
            //获取图片保存的相对路径【原图和缩略图】，=情侣相册路径/图片id
            String[] relativePathAndthumbImagePath = UploadImageFileUtils.savaImageFile(photoFiles[1], photoPath, UploadImageFileUtils.UploadImageType.LOVETIME_IMAGES);
            //更新图片路径信息
            lovetime.setImg2(relativePathAndthumbImagePath[0]);
            lovetime.setImgThumb2(relativePathAndthumbImagePath[1]);
        }
        if (photoFiles.length >= 3) {
            //获取图片保存的相对路径【原图和缩略图】，=情侣相册路径/图片id
            String[] relativePathAndthumbImagePath = UploadImageFileUtils.savaImageFile(photoFiles[2], photoPath, UploadImageFileUtils.UploadImageType.LOVETIME_IMAGES);
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
     * @param isPublic  是否只能获取公开的
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
            else if (isPublic&&lovetime.getState()==1) {
                //只能获取公开的，且这条记录是公开的
                isFlag = true;
            } else {
                //情侣之间可以查询私密状态的
                Lover lover = loverDao.findLover(user.getId());
                if (lover!=null&&(lovetime.getUser().getId() == lover.getMan().getId()
                        || lovetime.getUser().getId() == lover.getWomen().getId()))
                    isFlag = true;
            }
        }
        if(isFlag){
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
        if (lovetime.getImg1() != null) {
            lovetime.setImg1(WebConfig.getLovetimeResourceServerName() + lovetime.getImg1());
        }
        if (lovetime.getImg2() != null) {
            lovetime.setImg2(WebConfig.getLovetimeResourceServerName() + lovetime.getImg2());
        }
        if (lovetime.getImg3() != null) {
            lovetime.setImg3(WebConfig.getLovetimeResourceServerName() + lovetime.getImg3());
        }
        if (lovetime.getImgThumb1() != null) {
            lovetime.setImgThumb1(WebConfig.getLovetimeResourceServerName() + lovetime.getImgThumb1());
        }
        if (lovetime.getImgThumb2() != null) {
            lovetime.setImgThumb2(WebConfig.getLovetimeResourceServerName() + lovetime.getImgThumb2());
        }
        if (lovetime.getImgThumb3() != null) {
            lovetime.setImgThumb3(WebConfig.getLovetimeResourceServerName() + lovetime.getImgThumb3());
        }
    }
}
