package wjy.weiai7lv.dao;

import org.apache.ibatis.annotations.Param;
import wjy.weiai7lv.entity.AddressArea;
import wjy.weiai7lv.entity.AddressCity;
import wjy.weiai7lv.entity.AddressProvince;


public interface AddressDao {

    void savaProvince(AddressProvince Province);

    void savaCity(AddressCity city);

    void savaArea(AddressArea area);
    void savaAreaPro(AddressArea area);

    AddressArea getArea(@Param("areaId") int areaId);

    AddressProvince getProvince(@Param("proId") int proId);

    AddressCity getCity(@Param("cityId") int cityId);
}
