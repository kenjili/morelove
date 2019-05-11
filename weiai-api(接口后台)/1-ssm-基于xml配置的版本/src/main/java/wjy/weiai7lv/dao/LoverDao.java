package wjy.weiai7lv.dao;

import org.apache.ibatis.annotations.Param;
import wjy.weiai7lv.entity.Lover;

public interface LoverDao {

    int registLover(Lover lover);

    Lover findLover(@Param("userId") int userId);

    int updateLover(Lover lover);
}
