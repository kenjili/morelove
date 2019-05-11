package wjy.weiai7lv.entity;

import java.io.Serializable;

/**
 * 地址：省份
 */
public class AddressProvince implements Serializable {

    private int id;
    private String proName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProName() {
        return proName;
    }

    public void setProName(String proName) {
        this.proName = proName;
    }
}
