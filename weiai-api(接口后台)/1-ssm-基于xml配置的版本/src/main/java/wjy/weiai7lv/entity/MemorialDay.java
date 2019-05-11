package wjy.weiai7lv.entity;


import java.util.Date;

/**
 * 纪念日
 * @author wjy
 */
public class MemorialDay {

    private Integer id;
    private Lover lover;
    private String memorialName;
    private Date memorialDate;
    private int priority;//重要等级，排序使用
    private String image;//背景图片
    private Date createDatetime;
    private Date updateDatetime;

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

    public String getMemorialName() {
        return memorialName;
    }

    public void setMemorialName(String memorialName) {
        this.memorialName = memorialName;
    }

    public Date getMemorialDate() {
        return memorialDate;
    }

    public void setMemorialDate(Date memorialDate) {
        this.memorialDate = memorialDate;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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
