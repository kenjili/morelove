package com.wujiuye.weiai7lv.entity;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 相册
 * @author wjy
 */
@Data
public class Album {

    private Integer id;
    private Lover lover;
    private String albumName;
    private Date createDatetime;
    private Date updateDatetime;
    private List<Photos> photosList;

}
