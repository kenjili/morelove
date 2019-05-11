package com.wujiuye.weiai7lv.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.wujiuye.weiai7lv.dao.AddressDao;
import com.wujiuye.weiai7lv.dao.ScenicAreaDao;
import com.wujiuye.weiai7lv.entity.*;
import com.wujiuye.weiai7lv.service.ScenicAreaService;
import com.wujiuye.weiai7lv.utils.StringUtils;
import com.wujiuye.weiai7lv.utils.showapi.MyShowApiRequest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")//让idea对@Autowired等自动注入不警告
@Service
@Transactional
public class ScenicAreaServiceImpl implements ScenicAreaService {


    @Autowired
    private ScenicAreaDao scenicAreaDao;
    @Autowired
    private AddressDao addressDao;

    @Override
    public List<ScenicArea> searchScenicArea(String keyword, int page, int onePageRecordCount) {
        page = page < 1 ? 1 : page;
        onePageRecordCount = onePageRecordCount < 10 ? 10 : onePageRecordCount;
        List<ScenicArea> list = scenicAreaDao.searchScenicArea(keyword, (page - 1) * onePageRecordCount, onePageRecordCount);
        if (list == null || list.size() == 0) {
            //从第三方接口获取
            list = searchScenicAreaWithApi(keyword, 0, 0, 0, page);
        }
        return list;
    }

    @Override
    public List<ScenicArea> getScenicArea(int page, int onePageRecordCount) {
        page = page < 1 ? 1 : page;
        onePageRecordCount = onePageRecordCount < 10 ? 10 : onePageRecordCount;
        List<ScenicArea> list = scenicAreaDao.getScenicArea((page - 1) * onePageRecordCount, onePageRecordCount);
        if (list == null || list.size() == 0) {
            //从第三方接口获取
            list = searchScenicAreaWithApi("", 0, 0, 0, page);
        }
        return list;
    }


    @Override
    public List<ScenicArea> searchScenicAreaWithArea(int areaId, int page, int onePageRecordCount) {
        page = page < 1 ? 1 : page;
        onePageRecordCount = onePageRecordCount < 10 ? 10 : onePageRecordCount;
        AddressArea addressArea = addressDao.getArea(areaId);
        if (addressArea == null) return null;
        List<ScenicArea> list = scenicAreaDao.searchScenicAreaWithArea(addressArea, (page - 1) * onePageRecordCount, onePageRecordCount);
        if (list == null || list.size() == 0) {
            //从第三方接口获取
            list = searchScenicAreaWithApi(null, addressArea.getId(), 0, 0, page);
        }
        return list;
    }

    @Override
    public List<ScenicArea> searchScenicAreaWithCity(int cityId, int page, int onePageRecordCount) {
        page = page < 1 ? 1 : page;
        onePageRecordCount = onePageRecordCount < 10 ? 10 : onePageRecordCount;
        if (cityId < 0) return null;
        List<ScenicArea> list = scenicAreaDao.searchScenicAreaWithCity(cityId, (page - 1) * onePageRecordCount, onePageRecordCount);
        if (list == null || list.size() == 0) {
            //从第三方接口获取
            list = searchScenicAreaWithApi(null, 0, cityId, 0, page);
        }
        return list;
    }

    @Override
    public List<ScenicArea> searchScenicAreaWithPro(int proId, int page, int onePageRecordCount) {
        page = page < 1 ? 1 : page;
        onePageRecordCount = onePageRecordCount < 10 ? 10 : onePageRecordCount;
        if (proId < 0) return null;
        List<ScenicArea> list = scenicAreaDao.searchScenicAreaWithPro(proId, (page - 1) * onePageRecordCount, onePageRecordCount);
        if (list == null || list.size() == 0) {
            //从第三方接口获取
            list = searchScenicAreaWithApi(null, 0, 0, proId, page);
        }
        return list;
    }


    /**
     * 从第三方接口获取，并将获取到的结果保存到本地
     *
     * @param keyword
     * @param pAreaId
     * @param page
     * @return
     */
    private List<ScenicArea> searchScenicAreaWithApi(String keyword, int pAreaId, int pProId, int pCityId, int page) {
        MyShowApiRequest myShowApiRequest = new MyShowApiRequest(MyShowApiRequest.JINDIAN_API);
        if (!StringUtils.strIsNull(keyword))
            myShowApiRequest.addTextPara("keyword", keyword);
        if (pAreaId != 0)
            myShowApiRequest.addTextPara("areaId", pAreaId + "");
        if (pProId != 0)
            myShowApiRequest.addTextPara("proId", pProId + "");
        if (pCityId != 0)
            myShowApiRequest.addTextPara("cityId", pCityId + "");
        myShowApiRequest.addTextPara("page", page + "");
        String result = myShowApiRequest.post();
        if (result == null || result.trim().equals("")) return null;
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = null;
        try {
            rootNode = mapper.readTree(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (rootNode == null) return null;
        int errorCode = rootNode.path("showapi_res_code").asInt();
        if (errorCode == 0) {
            List<ScenicArea> scenicAreaList = new ArrayList<>();
            JsonNode listNode = rootNode.path("showapi_res_body").path("pagebean").path("contentlist");
            for (int i = 0; i < listNode.size(); i++) {
                JsonNode scenicNode = listNode.get(i);
                ScenicArea scenicArea = new ScenicArea();
                scenicArea.setId(scenicNode.path("id").asInt());

                //保证地区不为空
                AddressArea addressArea = addressDao.getArea(scenicNode.path("areaId").asInt());
                if (addressArea == null) {
                    addressArea = new AddressArea();
                    addressArea.setId(scenicNode.path("areaId").asInt());
                    addressArea.setAreaName(scenicNode.path("areaName").asText());
                    //保证省份不为空
                    AddressProvince province = addressDao.getProvince(scenicNode.path("proId").asInt());
                    if (province == null) {
                        province = new AddressProvince();
                        province.setId(scenicNode.path("proId").asInt());
                        province.setProName(scenicNode.path("proName").asText());
                        addressDao.savaProvince(province);
                    }
                    addressArea.setProvince(province);
                    //保证城市不为空
                    int cityId = scenicNode.path("cityId").asInt();
                    if (cityId > 0) {
                        AddressCity addressCity = addressDao.getCity(cityId);
                        if (addressCity == null) {
                            addressCity = new AddressCity();
                            addressCity.setId(cityId);
                            addressCity.setCityName(scenicNode.path("cityName").asText());
                            addressCity.setProvince(province);
                            addressDao.savaCity(addressCity);
                        }
                        addressArea.setCity(addressCity);
                    }
                    if (addressArea.getCity() == null)
                        addressDao.savaAreaPro(addressArea);
                    else
                        addressDao.savaArea(addressArea);
                }
                scenicArea.setArea(addressArea);
                scenicArea.setAddress(scenicNode.path("address").asText());
                scenicArea.setScenicName(scenicNode.path("name").asText());
                scenicArea.setSummary(scenicNode.path("summary").asText());
                scenicArea.setLocationX(scenicNode.path("location").path("lon").asDouble());
                scenicArea.setLocationY(scenicNode.path("location").path("lat").asDouble());
                scenicAreaDao.savaScenicArea(scenicArea);
                //保存景点图片
                JsonNode picListNode = scenicNode.path("picList");
                List<ScenicAreaImage> images = new ArrayList<>();
                for (int k = 0; k < picListNode.size(); k++) {
                    JsonNode picNode = picListNode.get(k);
                    ScenicAreaImage image = new ScenicAreaImage();
                    image.setScenicArea(scenicArea);
                    image.setPicUrl(picNode.path("picUrl").asText());
                    image.setPicUrlSmall(picNode.path("picUrlSmall").asText());
                    scenicAreaDao.savaScenicAreaImage(image);
                    images.add(image);
                }
                scenicArea.setImages(images);
                scenicAreaList.add(scenicArea);
            }
            return scenicAreaList;
        }
        return null;
    }
}
