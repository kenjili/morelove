package com.wujiuye.weiai7lv.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 地址：城市
 */
@Data
public class AddressCity implements Serializable {

    private int id;
    private String cityName;
    private AddressProvince province;

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof AddressCity))return false;
        if(obj==null)return false;
        return this.getId()==((AddressCity)obj).getId();
    }
}
