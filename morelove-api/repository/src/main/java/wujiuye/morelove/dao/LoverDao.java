package wujiuye.morelove.dao;

import wujiuye.morelove.pojo.Lover;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LoverDao {

    int registLover(Lover lover);

    Lover findLover(@Param("userId") int userId);

    int updateLover(Lover lover);
}
