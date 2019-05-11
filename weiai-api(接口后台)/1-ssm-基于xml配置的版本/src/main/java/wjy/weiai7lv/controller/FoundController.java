package wjy.weiai7lv.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import wjy.weiai7lv.entity.MovieBoxOffice;
import wjy.weiai7lv.entity.ScenicArea;
import wjy.weiai7lv.exception.ResponseResultConfig;
import wjy.weiai7lv.exception.WebResult;
import wjy.weiai7lv.service.MovieBoxOfficeService;
import wjy.weiai7lv.service.ScenicAreaService;
import wjy.weiai7lv.utils.DateTimeUtils;
import wjy.weiai7lv.utils.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 发现
 *
 * @author wjy
 */
@Controller
@RequestMapping("found")
public class FoundController {

    @Autowired
    protected ScenicAreaService scenicAreaService;
    @Autowired
    protected MovieBoxOfficeService movieBoxOfficeService;

    /**
     * 景点推荐
     * 五个可选参数，如果不指定则随机推荐
     *
     * @return
     */
    @RequestMapping(value = "scenicareas", method = RequestMethod.POST)
    @ResponseBody
    public WebResult foundAttractions(
            @RequestParam(value = "keyword", required = false) String keyword,//景点关键字
            @RequestParam(value = "proId", required = false) Integer proId,//省
            @RequestParam(value = "cityId", required = false) Integer cityId,//市
            @RequestParam(value = "areaId", required = false) Integer areaId,//镇，区，县
            @RequestParam(value = "page", required = false) Integer page//分页，当前页
    ) {
        List<ScenicArea> scenicAreaList = null;
        if (page == null) page = 1;
        if (!StringUtils.strIsNull(keyword)) {
            scenicAreaList = scenicAreaService.searchScenicArea(keyword, page, 10);
        } else if (areaId != null) {
            scenicAreaList = scenicAreaService.searchScenicAreaWithArea(areaId, page, 10);
        } else if (proId != null) {
            scenicAreaList = scenicAreaService.searchScenicAreaWithCity(cityId, page, 10);
        } else if (cityId != null) {
            scenicAreaList = scenicAreaService.searchScenicAreaWithPro(proId, page, 10);
        } else {
            scenicAreaList = scenicAreaService.getScenicArea(page, 10);
        }
        WebResult webResult = ResponseResultConfig.getResponseResult(ResponseResultConfig.ResponseResult.SUCCESS);
        webResult.setData(scenicAreaList);
        return webResult;
    }


    /**
     * 电影票房
     *
     * @return
     */
    @RequestMapping("movie")
    @ResponseBody
    public WebResult foundMovie() {
        //获取当前月份
        int month = DateTimeUtils.getCurrentMonth();
        int year = DateTimeUtils.getCurrentYear();
        int day = DateTimeUtils.getCurrentDay();
        int hour = 12;
        try {
            hour = Integer.valueOf(DateTimeUtils.getCurrentTime().split(" ")[1].split(":")[0]);
        } catch (Exception e) {
        }
        List<MovieBoxOffice> hourBoxOfficeList = movieBoxOfficeService.queryHourBoxOffice(year, month, day, hour);
        List<MovieBoxOffice> dayBoxOfficeList = movieBoxOfficeService.queryDayBoxOffice(year, month, day);
        List<MovieBoxOffice> monthBoxOfficeList = movieBoxOfficeService.queryMonthBoxOffice(year, month);
        WebResult webResult = ResponseResultConfig.getResponseResult(ResponseResultConfig.ResponseResult.SUCCESS);
        Map<String, Object> map = new HashMap<>();
        map.put("hourBoxOffice", hourBoxOfficeList);
        map.put("dayBoxOffice", dayBoxOfficeList);
        map.put("monthBoxOffice", monthBoxOfficeList);
        webResult.setData(map);
        return webResult;
    }


}
