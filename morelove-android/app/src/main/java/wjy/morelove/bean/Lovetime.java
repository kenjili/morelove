package wjy.morelove.bean;

import java.io.Serializable;
import java.util.Date;

import wjy.morelove.Jyson.JCName;
import wjy.morelove.Jyson.JFName;

/**
 * 恋爱时光
 * @author wjy
 */
@JCName(value = "data",type = 2)
public class Lovetime implements Serializable{

    private Integer id;
    @JFName("user")
    private User user;
    private String details;
    private String imgThumb1;
    private String imgThumb2;
    private String imgThumb3;
    private String img1;
    private String img2;
    private String img3;
    private int state;
    private int accessCount;
    private Date createDatetime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getImgThumb1() {
        return imgThumb1;
    }

    public void setImgThumb1(String imgThumb1) {
        this.imgThumb1 = imgThumb1;
    }

    public String getImgThumb2() {
        return imgThumb2;
    }

    public void setImgThumb2(String imgThumb2) {
        this.imgThumb2 = imgThumb2;
    }

    public String getImgThumb3() {
        return imgThumb3;
    }

    public void setImgThumb3(String imgThumb3) {
        this.imgThumb3 = imgThumb3;
    }

    public String getImg1() {
        return img1;
    }

    public void setImg1(String img1) {
        this.img1 = img1;
    }

    public String getImg2() {
        return img2;
    }

    public void setImg2(String img2) {
        this.img2 = img2;
    }

    public String getImg3() {
        return img3;
    }

    public void setImg3(String img3) {
        this.img3 = img3;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public Date getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(Date createDatetime) {
        this.createDatetime = createDatetime;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getAccessCount() {
        return accessCount;
    }

    public void setAccessCount(int accessCount) {
        this.accessCount = accessCount;
    }
}
