package wjy.morelove.bean;

import java.io.Serializable;

import wjy.morelove.Jyson.JCName;
import wjy.morelove.Jyson.JFName;

/**
 * 绑定状态
 */
@JCName("data")
public class LoverBindState implements Serializable {

    @JFName("state")
    private int state;
    @JFName("otherUser")
    private User otherUser;

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public User getOtherUser() {
        return otherUser;
    }

    public void setOtherUser(User otherUser) {
        this.otherUser = otherUser;
    }
}
