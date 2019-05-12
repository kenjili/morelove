package wjy.morelove.bean;


import java.io.Serializable;
import java.util.List;

import wjy.morelove.Jyson.JCName;
import wjy.morelove.Jyson.JFName;

/**
 * 旅游景点
 * @author wjy
 */
@JCName(value = "data",type = 1)
public class ScenicArea implements Serializable {

    private int id;
    private String scenicName;//景点名称
    private String address;//景点地址
    private Double locationX;//地图上的经纬度坐标
    private Double locationY;//地图上的经纬度坐标
    private String summary;//景点描述
    @JFName("images")
    private List<ScenicAreaImage> images;//图片列表

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getScenicName() {
        return scenicName;
    }

    public void setScenicName(String scenicName) {
        this.scenicName = scenicName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getLocationX() {
        return locationX;
    }

    public void setLocationX(Double locationX) {
        this.locationX = locationX;
    }

    public Double getLocationY() {
        return locationY;
    }

    public void setLocationY(Double locationY) {
        this.locationY = locationY;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public List<ScenicAreaImage> getImages() {
        return images;
    }

    public void setImages(List<ScenicAreaImage> images) {
        this.images = images;
    }
}
