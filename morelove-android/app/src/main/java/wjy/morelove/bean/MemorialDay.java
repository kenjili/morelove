package wjy.morelove.bean;


import java.util.Date;

import wjy.morelove.Jyson.JCName;

/**
 * 纪念日
 * @author wjy
 */
@JCName(value = "data",type = 2)
public class MemorialDay {

    private Integer id;
    private String memorialName;
    private Date memorialDate;
    private int priority;//重要等级，排序使用
    private String image;//背景图片

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

}
