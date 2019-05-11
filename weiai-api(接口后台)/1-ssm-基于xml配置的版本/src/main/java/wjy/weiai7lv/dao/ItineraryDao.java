package wjy.weiai7lv.dao;

import org.apache.ibatis.annotations.Param;
import wjy.weiai7lv.entity.Itinerary;

import java.util.List;

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
    List<Itinerary> listItinerary(@Param("loveId") int loveId,@Param("start") int start, @Param("length") int length);

    /**
     * 查询某个旅行记详情
     * @param id
     * @return
     */
    Itinerary queryItinerary(int id);

}
