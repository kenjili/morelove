package wjy.weiai7lv.dao;

import org.apache.ibatis.annotations.Param;
import wjy.weiai7lv.entity.Photos;

import java.util.List;

public interface PhotosDao {

    int uploadPhotos(Photos photos);

    int updatePhotos(Photos photos);

    List<Photos> photosWithAlbum(@Param("albumId") int albumId);
}
