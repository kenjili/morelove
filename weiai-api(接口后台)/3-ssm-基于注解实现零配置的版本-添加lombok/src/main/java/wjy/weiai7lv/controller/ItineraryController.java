package wjy.weiai7lv.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import wjy.weiai7lv.entity.Album;
import wjy.weiai7lv.entity.Itinerary;
import wjy.weiai7lv.exception.ResponseResultConfig;
import wjy.weiai7lv.exception.WebResult;
import wjy.weiai7lv.service.ItineraryService;

import java.util.ArrayList;
import java.util.List;

/**
 * 旅行日记，一个旅行日记对应一个相册，分页获取旅行日记相册
 *
 * @author wjy
 */
@Controller
@RequestMapping("itinerary")
public class ItineraryController {


    @Autowired
    private ItineraryService itineraryService;

    /**
     * 发表旅行日记，上传旅行记相册
     * @param title     旅行记标题
     * @param context   旅行记内容
     * @param address   相册拍摄的地点，地点名称/经纬度字符串，如：桂林-阳朔-十里长廊，客户端用定位获取到
     * @param photoFiles    相册的图片文件
     * @return
     */
    @RequestMapping(value = "public",method = RequestMethod.POST)
    @ResponseBody
    public WebResult uploadItinerary(@RequestParam(value = "title",required = false) String title,
                                     @RequestParam(value = "details",required = false) String context,
                                     @RequestParam(value = "address",required = false) String address,
                                     @RequestParam(value = "photos",required = false) MultipartFile[] photoFiles) {
        if (photoFiles != null && photoFiles.length != 0) {
            Itinerary itinerary = new Itinerary();
            itinerary.setDetails(context);
            itinerary.setAddress(address);
            itinerary.setTitle(title);
            Album album = new Album();
            //默认相册名称等于旅行记标题，因为一篇旅行日记对应一个相册
            album.setAlbumName(title);
            Itinerary itineraryResult = itineraryService.publicItinerary(itinerary,album,photoFiles);
            WebResult webResult = ResponseResultConfig.getResponseResult(ResponseResultConfig.ResponseResult.SUCCESS);
            webResult.setData(itineraryResult);
            return webResult;
        }
        WebResult webResult = ResponseResultConfig.getResponseResult(ResponseResultConfig.ResponseResult.EXCEPTION);
        webResult.setErrorMessage("请至少上传一张照片！");
        return webResult;
    }

    /**
     * 旅行日记列表
     * @param onePageSize 每页大小
     * @param currentPage 当前页
     * @return
     */
    @RequestMapping("/list/{onePageSize}/{currentPage}")
    @ResponseBody
    public WebResult itineraryWithPage(@PathVariable("onePageSize") int onePageSize,
                                       @PathVariable("currentPage") int currentPage){
        List<Itinerary> itineraries = itineraryService.listItinerary(onePageSize,currentPage);
        WebResult webResult = ResponseResultConfig.getResponseResult(ResponseResultConfig.ResponseResult.SUCCESS);
        webResult.setData(itineraries==null?new ArrayList<Itinerary>():itineraries);
        return webResult;
    }


    /**
     * 旅行日记详情
     * @param id
     * @return
     */
    @RequestMapping("/details/{id}")
    @ResponseBody
    public WebResult itineraryDetails(@PathVariable("id") int id){
        Itinerary itinerary = itineraryService.queryItinerary(id);
        WebResult webResult = ResponseResultConfig.getResponseResult(ResponseResultConfig.ResponseResult.SUCCESS);
        webResult.setData(itinerary);
        return webResult;
    }

}
