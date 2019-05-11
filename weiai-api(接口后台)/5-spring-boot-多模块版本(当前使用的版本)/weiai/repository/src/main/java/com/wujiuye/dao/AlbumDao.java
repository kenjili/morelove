package com.wujiuye.dao;

import com.wujiuye.pojo.Album;
import org.springframework.stereotype.Repository;

@Repository
public interface AlbumDao {

    int createAlbum(Album album);

    Album queryAlbum(int id);
}
