package com.wujiuye.dao;

import com.wujiuye.pojo.AddressArea;
import com.wujiuye.pojo.ScenicArea;
import com.wujiuye.pojo.ScenicAreaImage;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScenicAreaDao {

    /**
     * 保存景点图片
     * @param image
     */
    void savaScenicAreaImage(ScenicAreaImage image);

    /**
     * 保存景点
     * @param scenicArea
     */
    void savaScenicArea(ScenicArea scenicArea);

    /**
     * 获取默认显示的景点
     * @return
     */
    List<ScenicArea> getScenicArea(@Param("start") int start, @Param("count") int count);


    /**
     * 搜索景点
     * @param addressArea
     * @return
     */
    List<ScenicArea> searchScenicAreaWithArea(@Param("area") AddressArea addressArea, @Param("start") int start, @Param("count") int count);


    /**
     * 搜索景点
     * @param cityId
     * @return
     */
    List<ScenicArea> searchScenicAreaWithCity(@Param("cityId") int cityId, @Param("start") int start, @Param("count") int count);


    /**
     * 搜索景点
     * @param proId
     * @return
     */
    List<ScenicArea> searchScenicAreaWithPro(@Param("proId") int proId, @Param("start") int start, @Param("count") int count);


    /**
     * 关键词搜索
     * @param keyword
     * @return
     */
    List<ScenicArea> searchScenicArea(@Param("keyword") String keyword, @Param("start") int start, @Param("count") int count);

}
