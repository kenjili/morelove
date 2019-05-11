package com.wujiuye.dao;

import com.wujiuye.pojo.Lover;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
public interface LoverDao {

    int registLover(Lover lover);

    Lover findLover(@Param("userId") int userId);

    int updateLover(Lover lover);
}
