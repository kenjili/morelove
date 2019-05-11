package com.wujiuye.pojo;

import lombok.Data;

import java.io.Serializable;


/**
 * 旅游景点的图片
 */
@Data
public class ScenicAreaImage implements Serializable {

    private int id;
    private String picUrl;
    private String picUrlSmall;
    private ScenicArea scenicArea;
}
