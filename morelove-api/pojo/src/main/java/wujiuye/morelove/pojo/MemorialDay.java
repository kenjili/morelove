package wujiuye.morelove.pojo;


import lombok.Data;

import java.util.Date;

/**
 * 纪念日
 * @author wjy
 */
@Data
public class MemorialDay {

    private Integer id;
    private Lover lover;
    private String memorialName;
    private Date memorialDate;
    private int priority;//重要等级，排序使用
    private String image;//背景图片
    private Date createDatetime;
    private Date updateDatetime;

}
