package wujiuye.morelove.chat.packet.bean;

import java.io.Serializable;

/**
 * 模拟http请求的session
 *
 * @author wjy
 */
public class User implements Serializable {

    private Integer id;
    // 用户唯一性标识
    private String username;
    private String password;

    private String headThumb;

    public User() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getHeadThumb() {
        return headThumb;
    }

    public void setHeadThumb(String headThumb) {
        this.headThumb = headThumb;
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
}
