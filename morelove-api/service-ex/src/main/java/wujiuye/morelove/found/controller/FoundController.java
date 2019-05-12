package wujiuye.morelove.found.controller;


import wujiuye.morelove.common.exception.ResponseResultConfig;
import wujiuye.morelove.common.exception.WebResult;
import wujiuye.morelove.common.utils.DateTimeUtils;
import wujiuye.morelove.common.utils.StringUtils;
import wujiuye.morelove.found.service.MovieBoxOfficeAsyncService;
import wujiuye.morelove.found.service.ScenicAreaService;
import wujiuye.morelove.pojo.MovieBoxOffice;
import wujiuye.morelove.pojo.ScenicArea;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;


/**
 * 发现
 *
 * @author wjy
 */
@RestController
@RequestMapping("found")
@Slf4j
public class FoundController {

    @Autowired
    protected ScenicAreaService scenicAreaService;
    @Autowired
    protected MovieBoxOfficeAsyncService movieBoxOfficeAsyncService;

    /**
     * 景点推荐
     * 五个可选参数，如果不指定则随机推荐
     *
     * @return
     */
    @PostMapping("scenicareas")
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
    @GetMapping("movie")
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
        Future<List<MovieBoxOffice>> hourResult = movieBoxOfficeAsyncService.hourBoxOfficeList(year, month, day, hour);
        Future<List<MovieBoxOffice>> dayResult = movieBoxOfficeAsyncService.dayBoxOfficeList(year, month, day);
        Future<List<MovieBoxOffice>> monthResult = movieBoxOfficeAsyncService.monthBoxOfficeList(year, month);
        WebResult webResult = ResponseResultConfig.getResponseResult(ResponseResultConfig.ResponseResult.SUCCESS);
        //对查询结果进行整合
        Map<String, Object> map = new HashMap<>();
        try {
            map.put("hourBoxOffice", hourResult.get(3, TimeUnit.SECONDS));
            map.put("dayBoxOffice", dayResult.get(1, TimeUnit.SECONDS));
            map.put("monthBoxOffice", monthResult.get(1, TimeUnit.SECONDS));
        } catch (Exception e) {
            log.error("/found/foundMovie ===> " + e.getMessage());
        }
        webResult.setData(map);
        return webResult;
    }

}
