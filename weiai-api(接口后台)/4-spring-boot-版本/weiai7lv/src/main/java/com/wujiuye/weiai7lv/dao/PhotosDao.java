package com.wujiuye.weiai7lv.dao;

import com.wujiuye.weiai7lv.entity.Photos;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PhotosDao {

    int uploadPhotos(Photos photos);

    int updatePhotos(Photos photos);

    List<Photos> photosWithAlbum(@Param("albumId") int albumId);
}
