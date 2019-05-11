package wjy.weiai7lv.entity;

import java.io.Serializable;

/**
 * 地址：城市
 */
public class AddressCity implements Serializable {

    private int id;
    private String cityName;
    private AddressProvince province;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public AddressProvince getProvince() {
        return province;
    }

    public void setProvince(AddressProvince province) {
        this.province = province;
    }


    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof AddressCity))return false;
        if(obj==null)return false;
        return this.getId()==((AddressCity)obj).getId();
    }
}
