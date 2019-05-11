package wjy.weiai.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * 消息实体
 * @author wjy
 */
public class Message implements Serializable{

    private Integer id;
    private User sendUser;//发送方
    private User readUser;//接收方
    private Integer msgType;//消息类型
    private String message;//消息内容
    private int state;//消息状态、0为未读、1为已读、2为过期
    private Date createDate;//消息发送成功时间
    private Date overdueDate;//过期时间
    private int sendState = 0;//消息发送状态，0为发送中、1为发送成功、2为发送失败

    public int getSendState() {
        return sendState;
    }

    public void setSendState(int sendState) {
        this.sendState = sendState;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getSendUser() {
        return sendUser;
    }

    public void setSendUser(User sendUser) {
        this.sendUser = sendUser;
    }

    public User getReadUser() {
        return readUser;
    }

    public void setReadUser(User readUser) {
        this.readUser = readUser;
    }

    public Integer getMsgType() {
        return msgType;
    }

    public void setMsgType(Integer msgType) {
        this.msgType = msgType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getOverdueDate() {
        return overdueDate;
    }

    public void setOverdueDate(Date overdueDate) {
        this.overdueDate = overdueDate;
    }
}
