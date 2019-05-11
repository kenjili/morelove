package wjy.weiai7lv.dao;

import wjy.weiai7lv.entity.Album;

public interface AlbumDao {

    int createAlbum(Album album);

    Album queryAlbum(int id);
}
