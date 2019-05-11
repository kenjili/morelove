package com.wujiuye.dao;

import com.wujiuye.pojo.AddressArea;
import com.wujiuye.pojo.AddressCity;
import com.wujiuye.pojo.AddressProvince;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressDao {

    void savaProvince(AddressProvince Province);

    void savaCity(AddressCity city);

    void savaArea(AddressArea area);
    void savaAreaPro(AddressArea area);

    AddressArea getArea(@Param("areaId") int areaId);

    AddressProvince getProvince(@Param("proId") int proId);

    AddressCity getCity(@Param("cityId") int cityId);
}
