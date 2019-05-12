package wujiuye.morelove.pojo;

import lombok.Data;

import java.io.Serializable;

/**
 * 地址：区域
 */
@Data
public class AddressArea implements Serializable {

    private int id;
    private String areaName;
    private AddressCity city;
    private AddressProvince province;

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof AddressArea))return false;
        if(obj==null)return false;
        return this.getId()==((AddressArea)obj).getId();
    }
}
