package wjy.weiai7lv.entity;

import java.util.Date;
import java.util.List;

/**
 * 相册
 * @author wjy
 */
public class Album {

    private Integer id;
    private Lover lover;
    private String albumName;
    private Date createDatetime;
    private Date updateDatetime;
    private List<Photos> photosList;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Lover getLover() {
        return lover;
    }

    public void setLover(Lover lover) {
        this.lover = lover;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public Date getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(Date createDatetime) {
        this.createDatetime = createDatetime;
    }

    public Date getUpdateDatetime() {
        return updateDatetime;
    }

    public void setUpdateDatetime(Date updateDatetime) {
        this.updateDatetime = updateDatetime;
    }

    public List<Photos> getPhotosList() {
        return photosList;
    }

    public void setPhotosList(List<Photos> photosList) {
        this.photosList = photosList;
    }
}
