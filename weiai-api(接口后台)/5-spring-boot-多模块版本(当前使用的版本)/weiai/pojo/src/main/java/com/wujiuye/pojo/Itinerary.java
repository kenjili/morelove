package com.wujiuye.pojo;

import lombok.Data;

import java.util.Date;

/**
 * 旅行记
 * @author wjy
 */
@Data
public class Itinerary {

    private Integer id;
    private Lover lover;
    private User user;
    private String title;
    private String details;
    private String address;
    private Date createDatetime;
    private Date updateDatetime;
    private Album album;

}
