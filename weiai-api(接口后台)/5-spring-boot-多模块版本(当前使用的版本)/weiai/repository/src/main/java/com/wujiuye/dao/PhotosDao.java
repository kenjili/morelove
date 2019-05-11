package com.wujiuye.dao;

import com.wujiuye.pojo.Photos;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PhotosDao {

    int uploadPhotos(Photos photos);

    int updatePhotos(Photos photos);

    List<Photos> photosWithAlbum(@Param("albumId") int albumId);
}
