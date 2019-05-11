package com.wujiuye.pojo;

import lombok.Data;

import java.util.Date;

/**
 * 照片
 * @author wjy
 */
@Data
public class Photos {

    private Integer id;
    private User user;
    private Album album;
    private String img;
    private String imgThumb;
    private String imgDetails;
    private Date createDatetime;
    private Date updateDatetime;

}
