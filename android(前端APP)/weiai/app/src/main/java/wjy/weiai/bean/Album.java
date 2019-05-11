package wjy.weiai.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 相册
 * @author wjy
 */
public class Album implements Serializable{

    private Integer id;
    private String albumName;
    private Date createDatetime;
    private List<Photos> photosList;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public List<Photos> getPhotosList() {
        return photosList;
    }

    public void setPhotosList(List<Photos> photosList) {
        this.photosList = photosList;
    }
}
