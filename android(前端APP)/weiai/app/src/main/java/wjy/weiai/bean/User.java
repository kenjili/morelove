package wjy.weiai.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户
 * 需要缓存到redis中的bean需要实现序列化接口
 * @author wjy
 */
public class User implements Serializable {

    private Integer id;
    private String username;
    private String password;
    private String realname;
    private String headThumb;
    private int sex;//0为男，1为女
    private Date birthday;
    private String email;
    private boolean disable;
    private Date createDatetime;
    private Date updateDatetime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getHeadThumb() {
        return headThumb;
    }

    public void setHeadThumb(String headThumb) {
        this.headThumb = headThumb;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isDisable() {
        return disable;
    }

    public void setDisable(boolean disable) {
        this.disable = disable;
    }

    public Date getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(Date createDatetime) {
        this.createDatetime = createDatetime;
    }

    public Date getUpdateDatetime() {
        return updateDatetime;
    }

    public void setUpdateDatetime(Date updateDatetime) {
        this.updateDatetime = updateDatetime;
    }
}
