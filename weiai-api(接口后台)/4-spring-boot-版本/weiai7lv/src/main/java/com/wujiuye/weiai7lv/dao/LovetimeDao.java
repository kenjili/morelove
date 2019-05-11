package com.wujiuye.weiai7lv.dao;

import com.wujiuye.weiai7lv.entity.Lover;
import com.wujiuye.weiai7lv.entity.Lovetime;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface LovetimeDao {

    int savaLovetime(Lovetime lovetime);

    List<Lovetime> queryLovetime(@Param("state") int state, @Param("start") int start, @Param("count") int pageSize, @Param("lover") Lover lover);

    List<Lovetime> queryPublicLovetime(@Param("start") int start, @Param("count") int pageSize);

    int updateLovetimeAccess(int lovetimeId);

    Lovetime getLovetime(int lovetimeId);
}
