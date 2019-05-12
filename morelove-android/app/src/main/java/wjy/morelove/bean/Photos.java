package wjy.morelove.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * 照片
 * @author wjy
 */
public class Photos implements Serializable{

    private Integer id;
    private User user;
    private Album album;
    private String img;
    private String imgThumb;
    private String imgDetails;
    private Date createDatetime;
    private Date updateDatetime;

    public Album getAlbum() {
        return album;
    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getImgThumb() {
        return imgThumb;
    }

    public void setImgThumb(String imgThumb) {
        this.imgThumb = imgThumb;
    }

    public String getImgDetails() {
        return imgDetails;
    }

    public void setImgDetails(String imgDetails) {
        this.imgDetails = imgDetails;
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
}
