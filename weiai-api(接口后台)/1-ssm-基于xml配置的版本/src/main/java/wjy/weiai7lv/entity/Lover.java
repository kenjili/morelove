package wjy.weiai7lv.entity;


import java.util.Date;

/**
 * 情侣
 * @author wjy
 */
public class Lover {

    private Integer id;
    private User man;
    private User women;
    private String details;
    private String loveImgThumb;
    private int state;
    private Date createDatetime;
    private Date updateDatetime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getMan() {
        return man;
    }

    public void setMan(User man) {
        this.man = man;
    }

    public User getWomen() {
        return women;
    }

    public void setWomen(User women) {
        this.women = women;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getLoveImgThumb() {
        return loveImgThumb;
    }

    public void setLoveImgThumb(String loveImgThumb) {
        this.loveImgThumb = loveImgThumb;
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

    public Date getUpdateDatetime() {
        return updateDatetime;
    }

    public void setUpdateDatetime(Date updateDatetime) {
        this.updateDatetime = updateDatetime;
    }
}
