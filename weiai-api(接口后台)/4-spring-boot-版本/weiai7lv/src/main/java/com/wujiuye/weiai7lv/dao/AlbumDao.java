package com.wujiuye.weiai7lv.dao;

import com.wujiuye.weiai7lv.entity.Album;

public interface AlbumDao {

    int createAlbum(Album album);

    Album queryAlbum(int id);
}
