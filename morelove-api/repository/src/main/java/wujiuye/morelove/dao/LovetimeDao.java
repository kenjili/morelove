package wujiuye.morelove.dao;

import wujiuye.morelove.pojo.Lover;
import wujiuye.morelove.pojo.Lovetime;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LovetimeDao {

    int savaLovetime(Lovetime lovetime);

    List<Lovetime> queryLovetime(@Param("state") int state, @Param("start") int start, @Param("count") int pageSize, @Param("lover") Lover lover);

    List<Lovetime> queryPublicLovetime(@Param("start") int start, @Param("count") int pageSize);

    int updateLovetimeAccess(int lovetimeId);

    Lovetime getLovetime(int lovetimeId);
}
