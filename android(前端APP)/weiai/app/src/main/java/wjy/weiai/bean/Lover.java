package wjy.weiai.bean;


import wjy.weiai.Jyson.JCName;

/**
 * 情侣
 * @author wjy
 */
@JCName("data")
public class Lover {

    private int id;
    private User man;
    private User women;
    private String details;
    private String loveImgThumb;
    private int state;

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

}
