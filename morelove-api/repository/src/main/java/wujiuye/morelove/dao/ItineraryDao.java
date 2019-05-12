package wujiuye.morelove.dao;

import wujiuye.morelove.pojo.Itinerary;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItineraryDao {

    /**
     * 发布旅行记
     * @param itinerary
     * @return
     */
    int publicItinerary(Itinerary itinerary);

    /**
     * 旅行记列表
     * @param loveId
     * @param start
     * @param length
     * @return
     */
    List<Itinerary> listItinerary(@Param("loveId") int loveId, @Param("start") int start, @Param("length") int length);

    /**
     * 查询某个旅行记详情
     * @param id
     * @return
     */
    Itinerary queryItinerary(int id);

}
