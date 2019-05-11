package wjy.weiai7lv.service;

import wjy.weiai7lv.entity.ScenicArea;

import java.util.List;

public interface ScenicAreaService {

    /**
     * 根据关键词搜索景点，如果不存在则调用第三方接口获取，获取到再存到本地
     * @param keyword
     * @return
     */
    List<ScenicArea> searchScenicArea(String keyword,int page,int onePageRecordCount);

    /**
     * 默认推荐景点
     * @param
     * @return
     */
    List<ScenicArea> getScenicArea(int page,int onePageRecordCount);

    /**
     * 根据地区获取该地区的景点
     * @param areaId
     * @return
     */
    List<ScenicArea> searchScenicAreaWithArea(int areaId,int page,int onePageRecordCount);

    /**
     * 根据城市获取该地区的景点
     * @param cityId
     * @return
     */
    List<ScenicArea> searchScenicAreaWithCity(int cityId,int page,int onePageRecordCount);

    /**
     * 根据省份获取该地区的景点
     * @param proId
     * @return
     */
    List<ScenicArea> searchScenicAreaWithPro(int proId,int page,int onePageRecordCount);
}
