package com.wujiuye.weiai7lv.dao;

import com.wujiuye.weiai7lv.entity.AddressArea;
import com.wujiuye.weiai7lv.entity.AddressCity;
import com.wujiuye.weiai7lv.entity.AddressProvince;
import org.apache.ibatis.annotations.Param;

public interface AddressDao {

    void savaProvince(AddressProvince Province);

    void savaCity(AddressCity city);

    void savaArea(AddressArea area);
    void savaAreaPro(AddressArea area);

    AddressArea getArea(@Param("areaId") int areaId);

    AddressProvince getProvince(@Param("proId") int proId);

    AddressCity getCity(@Param("cityId") int cityId);
}
