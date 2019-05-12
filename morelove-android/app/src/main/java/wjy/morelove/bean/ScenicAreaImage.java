package wjy.morelove.bean;

import java.io.Serializable;


/**
 * 旅游景点的图片
 */
public class ScenicAreaImage implements Serializable {

    private int id;
    private String picUrl;
    private String picUrlSmall;
    private ScenicArea scenicArea;

    public ScenicArea getScenicArea() {
        return scenicArea;
    }

    public void setScenicArea(ScenicArea scenicArea) {
        this.scenicArea = scenicArea;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getPicUrlSmall() {
        return picUrlSmall;
    }

    public void setPicUrlSmall(String picUrlSmall) {
        this.picUrlSmall = picUrlSmall;
    }
}
