package wujiuye.morelove.pojo;

import lombok.Data;

import java.util.Date;

/**
 * 恋爱时光
 * @author wjy
 */
@Data
public class Lovetime {

    private Integer id;
    private User user;
    private String details;
    private String imgThumb1;
    private String imgThumb2;
    private String imgThumb3;
    private String img1;
    private String img2;
    private String img3;
    private int state;
    private int accessCount;
    private Date createDatetime;
    private Date updateDatetime;

}
