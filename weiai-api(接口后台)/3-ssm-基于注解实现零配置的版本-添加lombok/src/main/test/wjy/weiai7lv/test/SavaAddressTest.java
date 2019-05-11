package wjy.weiai7lv.test;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import wjy.weiai7lv.dao.AddressDao;
import wjy.weiai7lv.entity.AddressArea;
import wjy.weiai7lv.entity.AddressCity;
import wjy.weiai7lv.entity.AddressProvince;
import wjy.weiai7lv.utils.showapi.MyShowApiRequest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/spring.xml")
public class SavaAddressTest {

    @Autowired
    private AddressDao addressDao;

    @Test
    public void savaAddress(){
        String pros = new MyShowApiRequest(MyShowApiRequest.PRO_LIST_API)
                .post();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = null;
        try {
            rootNode = mapper.readTree(pros);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(rootNode==null)return;
        int errorCode = rootNode.path("showapi_res_code").asInt();
        if (errorCode == 0) {
            JsonNode listNode = rootNode.path("showapi_res_body").path("list");
            for (int i = 0; i < listNode.size(); i++) {
                JsonNode proNode = listNode.get(i);
                int proId = proNode.path("id").asInt();
                String proName = proNode.path("name").asText();
                System.out.println("pro:[id:'" + proId + "',name:'" + proName + "']");
                AddressProvince addressProvince = new AddressProvince();
                addressProvince.setId(proId);
                addressProvince.setProName(proName);
                try{
                    addressDao.savaProvince(addressProvince);
                }catch (Exception e){
                    e.printStackTrace();
                }
                savaCity(addressProvince);
                try {
                    System.err.println("线程休眠中。。。。。");
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private void savaCity(AddressProvince province){
        //获取这个省份下面的城市
        String cityResult = new MyShowApiRequest(MyShowApiRequest.CITY_LIST_API)
                .addTextPara("proId", province.getId() + "")
                .post();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode cityRootNode = null;
        try {
            cityRootNode = mapper.readTree(cityResult);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(cityRootNode==null)return;
        int errorCode2 = cityRootNode.path("showapi_res_code").asInt();
        if (errorCode2 == 0) {
            JsonNode cityListNode = cityRootNode.path("showapi_res_body").path("list");
            List<AddressCity> cityList = new ArrayList<>();
            for (int j = 0; j < cityListNode.size(); j++) {
                JsonNode cityNode = cityListNode.get(j);
                //有些省份是没有市的
                if (!cityNode.has("cityId")) {
                    //直接就是地区了
                    int areaId = cityNode.path("id").asInt();
                    String areaName = cityNode.path("name").asText();
                    AddressArea area = new AddressArea();
                    area.setProvince(province);
                    area.setCity(null);
                    area.setId(areaId);
                    area.setAreaName(areaName);
                    try{
                        addressDao.savaArea(area);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    System.out.println("area:[id:'" + areaId + "',areaName:'" + areaName + "']");
                    continue;
                }
                int cityId = cityNode.path("cityId").asInt();
                String cityName = cityNode.path("cityName").asText();
                AddressCity city = new AddressCity();
                city.setProvince(province);
                city.setId(cityId);
                city.setCityName(cityName);
                if (cityList.contains(city))
                    continue;
                cityList.add(city);
                try{
                    addressDao.savaCity(city);
                }catch (Exception e){
                    e.printStackTrace();
                }
                System.out.println("city:[cityId:'" + cityId + "',cityName:'" + cityName + "']");
                savaArea(province,city);
                try {
                    System.err.println("线程休眠中。。。。。");
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    private void savaArea(AddressProvince province,AddressCity city){
        //获取这个城市下面的地区
        String areaResult = new MyShowApiRequest(MyShowApiRequest.AREA_LIST_API)
                .addTextPara("cityId", city.getId() + "")
                .post();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode areaRootNode = null;
        try {
            areaRootNode = mapper.readTree(areaResult);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(areaRootNode==null)return;
        int errorCode3 = areaRootNode.path("showapi_res_code").asInt();
        if (errorCode3 == 0) {
            JsonNode areaListNode = areaRootNode.path("showapi_res_body").path("list");
            List<AddressArea> areaList = new ArrayList<>();
            for (int k = 0; k < areaListNode.size(); k++) {
                JsonNode areaNode = areaListNode.get(k);
                if(!areaNode.has("id"))continue;
                int areaId = areaNode.path("id").asInt();
                String areaName = areaNode.path("name").asText();
                AddressArea area = new AddressArea();
                area.setProvince(province);
                area.setCity(city);
                area.setId(areaId);
                area.setAreaName(areaName);
                if (areaList.contains(area))
                    continue;
                areaList.add(area);
                try {
                    addressDao.savaArea(area);
                }catch (Exception e){
                    e.printStackTrace();
                }
                System.out.println("area:[id:'" + areaId + "',areaName:'" + areaName + "']");
            }
            System.out.println();
        }
    }
}
