package wujiuye.morelove.pojo;


import lombok.Data;

import java.util.Date;

/**
 * 情侣
 * @author wjy
 */
@Data
public class Lover {

    private Integer id;
    private User man;
    private User women;
    private String details;
    private String loveImgThumb;
    private int state;
    private Date createDatetime;
    private Date updateDatetime;
}
