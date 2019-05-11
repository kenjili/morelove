package wjy.weiai7lv.entity;

import java.io.Serializable;

/**
 * 地址：区域
 */
public class AddressArea implements Serializable {

    private int id;
    private String areaName;
    private AddressCity city;
    private AddressProvince province;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public AddressCity getCity() {
        return city;
    }

    public void setCity(AddressCity city) {
        this.city = city;
    }

    public AddressProvince getProvince() {
        return province;
    }

    public void setProvince(AddressProvince province) {
        this.province = province;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof AddressArea))return false;
        if(obj==null)return false;
        return this.getId()==((AddressArea)obj).getId();
    }
}
