package wujiuye.morelove.dao;

import wujiuye.morelove.pojo.Album;
import org.springframework.stereotype.Repository;

@Repository
public interface AlbumDao {

    int createAlbum(Album album);

    Album queryAlbum(int id);
}
