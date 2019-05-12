package wjy.morelove.bean;


import java.io.Serializable;

/**
 * app版本信息
 */
public class AppVersion implements Serializable{

    private int code;
    private String name;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
