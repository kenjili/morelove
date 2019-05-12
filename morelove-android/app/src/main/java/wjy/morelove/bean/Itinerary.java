package wjy.morelove.bean;

import java.io.Serializable;
import java.util.Date;

import wjy.morelove.Jyson.JCName;

/**
 * 旅行记
 * @author wjy
 */
@JCName(value = "data",type = 2)
public class Itinerary implements Serializable{

    private Integer id;
    private User user;
    private String title;
    private String details;
    private String address;
    private Date createDatetime;
    private Album album;

    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(Date createDatetime) {
        this.createDatetime = createDatetime;
    }

}
