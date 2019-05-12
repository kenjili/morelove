package wujiuye.morelove.pojo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户
 * 需要缓存到redis中的bean需要实现序列化接口
 *
 * @author wjy
 */
@Getter
@Setter
public class User implements Serializable {

    private Integer id;
    private String username;
    private String password;
    private String realname;
    private String headThumb;
    private int sex;//0为男，1为女
    private Date birthday;
    private String phoneNumber;
    private String email;//弃用,但保留，数据库中也会保留
    private boolean disable;
    private Date createDatetime;
    private Date updateDatetime;
}
