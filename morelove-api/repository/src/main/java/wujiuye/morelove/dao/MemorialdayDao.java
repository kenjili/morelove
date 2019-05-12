package wujiuye.morelove.dao;

import wujiuye.morelove.pojo.MemorialDay;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemorialdayDao {


    /**
     * 保存纪念日
     * @param memorialdayDao
     */
    void savaMemorialDay(MemorialDay memorialdayDao);

    /**
     * 删除纪念日
     * @param id
     */
    void delMemorialDay(@Param("id") int id, @Param("loverId") int loverId);

    /**
     * 获取某对情侣的所有纪念日
     * @param loverId
     */
    List<MemorialDay> getAllMemorialDay(@Param("loverId") int loverId);

    /**
     * 获取总数
     * @param loverId
     * @return
     */
    int getCount(@Param("loverId") int loverId);

}
