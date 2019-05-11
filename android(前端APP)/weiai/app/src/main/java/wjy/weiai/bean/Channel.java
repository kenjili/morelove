package wjy.weiai.bean;

import java.io.Serializable;

/**
 * 页面间数据传递的封装
 * @author wjy
 */
public class Channel implements Serializable{

    private String title;
    private Object tag;

    public Channel(String title, String tag) {
        this.title = title;
        this.tag = tag;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Object getTag() {
        return tag;
    }

    public void setTag(Object tag) {
        this.tag = tag;
    }
}
