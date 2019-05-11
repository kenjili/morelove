package com.wujiuye.weiai7lv.dao;

import com.wujiuye.weiai7lv.entity.Lover;
import org.apache.ibatis.annotations.Param;

public interface LoverDao {

    int registLover(Lover lover);

    Lover findLover(@Param("userId") int userId);

    int updateLover(Lover lover);
}
