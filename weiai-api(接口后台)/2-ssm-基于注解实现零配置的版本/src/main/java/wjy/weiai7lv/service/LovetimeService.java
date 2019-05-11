package wjy.weiai7lv.service;

import org.springframework.web.multipart.MultipartFile;
import wjy.weiai7lv.entity.Lovetime;

import java.util.List;

public interface LovetimeService {

    /**
     * 发表恋爱时光
     * @param context   内容
     * @param photoFiles    图片
     * @param isPublic  是否公开
     * @return
     */
    Lovetime publicLovetime(String context, MultipartFile[] photoFiles,boolean isPublic);


    /**
     * 仅情侣间可见的及情侣间发布的公开的
     * @param pageSize
     * @param page
     * @return
     */
    List<Lovetime> queryLovetime(int pageSize,int page);

    /**
     * 公开的，所有用户发表的
     * @param pageSize
     * @param page
     * @return
     */
    List<Lovetime> queryPublicLovetime(int pageSize,int page);

    /**
     * 获取详情
     * @param isPublic  是否只能查询公开的
     * @param lovetimeId
     */
    Lovetime lovetimeDetails(boolean isPublic,int lovetimeId);
}
