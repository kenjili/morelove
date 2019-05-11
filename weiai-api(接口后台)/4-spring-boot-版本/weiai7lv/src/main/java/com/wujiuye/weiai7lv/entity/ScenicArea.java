package com.wujiuye.weiai7lv.entity;


import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 旅游景点
 * @author wjy
 */
@Data
public class ScenicArea implements Serializable {

    private int id;
    private AddressArea area;//所在地区
    private String scenicName;//景点名称
    private String address;//景点地址
    private Double locationX;//地图上的经纬度坐标
    private Double locationY;//地图上的经纬度坐标
    private String summary;//景点描述
    private List<ScenicAreaImage> images;//图片列表

}
