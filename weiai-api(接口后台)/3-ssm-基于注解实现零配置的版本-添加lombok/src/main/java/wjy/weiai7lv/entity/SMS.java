package wjy.weiai7lv.entity;

import java.io.Serializable;

/**
 * 短信验证码，用于注册验证与找回密码验证
 * @author wjy
 */
public class SMS implements Serializable {

    private String phoneNumber;//手机号
    private String code;//验证码
    private long sendDateTime;//发送时间，单位为秒
    private int overdue;//有效期，单位为秒

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public long getSendDateTime() {
        return sendDateTime;
    }

    public void setSendDateTime(long sendDateTime) {
        this.sendDateTime = sendDateTime;
    }

    public int getOverdue() {
        return overdue;
    }

    public void setOverdue(int overdue) {
        this.overdue = overdue;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
