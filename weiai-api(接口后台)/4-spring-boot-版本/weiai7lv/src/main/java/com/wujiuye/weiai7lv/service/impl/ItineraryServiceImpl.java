package com.wujiuye.weiai7lv.service.impl;

import com.wujiuye.weiai7lv.config.WebConstantConfig;
import com.wujiuye.weiai7lv.dao.*;
import com.wujiuye.weiai7lv.entity.*;
import com.wujiuye.weiai7lv.service.ItineraryService;
import com.wujiuye.weiai7lv.utils.UploadImageFileUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


import java.util.List;

/**
 * 旅行记
 *
 * @author wjy
 */
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")//让idea对@Autowired等自动注入不警告
@Service
@Transactional
public class ItineraryServiceImpl implements ItineraryService {

    @Autowired
    private WebConstantConfig webConstantConfig;

    @Autowired
    private AlbumDao albumDao;

    @Autowired
    private LoverDao loverDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private ItineraryDao itineraryDao;

    @Autowired
    private PhotosDao photosDao;

    /**
     * 发布旅行记
     * 上传的相册不会改变其扩展名
     *
     * @param itinerary
     * @param album
     * @param photoFiles
     * @return
     */
    public Itinerary publicItinerary(Itinerary itinerary, Album album, MultipartFile[] photoFiles) {
        //获取通过shiro认证的用户信息
        String username = (String) SecurityUtils.getSubject().getPrincipal();
        User user = userDao.getUserWithUsername(username);
        Lover lover = loverDao.findLover(user.getId());
        //创建一个相册，获取到相册的id
        album.setLover(lover);
        albumDao.createAlbum(album);

        itinerary.setAlbum(album);
        itinerary.setUser(user);
        itinerary.setLover(lover);
        itineraryDao.publicItinerary(itinerary);

        //获取情侣相册相对路径，/情侣id/相册id
        String loverAlbumPath = "/lover-" + lover.getId() + "/album-" + album.getId();
        //一张张图片上传到相册目录下
        for (MultipartFile photo : photoFiles
        ) {
            Photos photos = new Photos();
            photos.setAlbum(album);
            photos.setUser(user);
            //保存photos获取id
            photosDao.uploadPhotos(photos);
            String photoPath = loverAlbumPath + "/photos-" + photos.getId();
            //获取图片保存的相对路径【原图和缩略图】，=情侣相册路径/图片id
            String[] relativePathAndthumbImagePath = UploadImageFileUtils.savaImageFile(photo, webConstantConfig.getPrivateUploadFileRootPath(), webConstantConfig.getAlbumRootPath() + photoPath);
            //更新图片路径信息
            photos.setImg(relativePathAndthumbImagePath[0]);
            photos.setImgThumb(relativePathAndthumbImagePath[1]);
            //更新photos
            photosDao.updatePhotos(photos);
        }
        //查询返回
        Itinerary newItinerary = itineraryDao.queryItinerary(itinerary.getId());
        for (Photos photo : newItinerary.getAlbum().getPhotosList()) {
            setImageUrl(photo);
        }
        return newItinerary;
    }

    public List<Itinerary> listItinerary(int pageSize, int currentPage) {
        //获取通过shiro认证的用户信息
        String username = (String) SecurityUtils.getSubject().getPrincipal();
        User user = userDao.getUserWithUsername(username);
        Lover lover = loverDao.findLover(user.getId());
        if (currentPage < 1) currentPage = 1;
        if (pageSize < 1) pageSize = 1;
        List<Itinerary> list = this.itineraryDao.listItinerary(lover.getId(), (currentPage - 1) * pageSize, pageSize);
        for (Itinerary itinerary : list) {
            for (Photos photo : itinerary.getAlbum().getPhotosList()) {
                setImageUrl(photo);
            }
        }
        return list;
    }

    public Itinerary queryItinerary(int id) {
        //获取通过shiro认证的用户信息
        String username = (String) SecurityUtils.getSubject().getPrincipal();
        User user = userDao.getUserWithUsername(username);
        Lover lover = loverDao.findLover(user.getId());
        Itinerary itinerary = itineraryDao.queryItinerary(id);
        if(itinerary==null)return null;
        //判断是否有权限访问
        if (itinerary.getLover().getId() == lover.getId()) {
            for (Photos photo : itinerary.getAlbum().getPhotosList()) {
                setImageUrl(photo);
            }
            return itinerary;
        }
        return null;
    }


    private void setImageUrl(Photos photo){
        if(photo==null)return;
        //修改图片路径为详细的网路访问路径
        photo.setImg(webConstantConfig.getPrivateResourceServerName() + photo.getImg());
        photo.setImgThumb(webConstantConfig.getPrivateResourceServerName() + photo.getImgThumb());
    }
}
