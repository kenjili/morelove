package wujiuye.morelove.lovetime.controller;


import wujiuye.morelove.common.exception.ResponseResultConfig;
import wujiuye.morelove.common.exception.WebResult;
import wujiuye.morelove.lovetime.service.LovetimeService;
import wujiuye.morelove.pojo.Lovetime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("lovetime")
public class LovetimeController {


    @Autowired
    private LovetimeService lovetimeService;

    /**
     * 发布恋爱时光
     *
     * @param context    内容
     * @param isPublic   是否公开
     * @param photoFiles 图片超过限制的张数就把超过的抛弃掉
     * @return
     */
    @RequestMapping(value = "public", method = RequestMethod.POST)
    @ResponseBody
    public WebResult publicLovetime(
            @RequestParam(value = "context", required = true) String context,
            @RequestParam(value = "isPublic", required = true) boolean isPublic,
            @RequestParam(value = "images", required = true) MultipartFile[] photoFiles
    ) {
        if (photoFiles.length == 0) {
            WebResult webResult = ResponseResultConfig.getResponseResult(ResponseResultConfig.ResponseResult.EXCEPTION);
            webResult.setErrorMessage("至少要上传一张图片哦！");
            return webResult;
        }
        Lovetime lovetime = lovetimeService.publicLovetime(context, photoFiles, isPublic);
        WebResult webResult = ResponseResultConfig.getResponseResult(ResponseResultConfig.ResponseResult.SUCCESS);
        webResult.setData(lovetime);
        return webResult;
    }


    /**
     * 浏览时光动态
     *
     * @param pageSize
     * @param currentPage
     * @return
     */
    @RequestMapping("list/{pageSize}/{page}")
    @ResponseBody
    public WebResult lovetimeList(
            @PathVariable("pageSize") int pageSize,
            @PathVariable("page") int currentPage
    ) {
        WebResult webResult = ResponseResultConfig.getResponseResult(ResponseResultConfig.ResponseResult.SUCCESS);
        webResult.setData(lovetimeService.queryLovetime(pageSize, currentPage));
        return webResult;
    }


    /**
     * 浏览晒一晒动态（公开的时光）
     *
     * @param pageSize
     * @param currentPage
     * @return
     */
    @RequestMapping("world/list/{pageSize}/{page}")
    @ResponseBody
    public WebResult publicLovetimeList(
            @PathVariable("pageSize") int pageSize,
            @PathVariable("page") int currentPage
    ) {
        WebResult webResult = ResponseResultConfig.getResponseResult(ResponseResultConfig.ResponseResult.SUCCESS);
        webResult.setData(lovetimeService.queryPublicLovetime(pageSize, currentPage));
        return webResult;
    }


    /**
     * 获取单条记录详情
     * 只能查询公开的
     * @return
     */
    @RequestMapping("world/details/{id}")
    @ResponseBody
    public WebResult publicLovetimeDetails(
            @PathVariable("id") int lovetimeId
    ) {
        WebResult webResult = ResponseResultConfig.getResponseResult(ResponseResultConfig.ResponseResult.SUCCESS);
        webResult.setData(lovetimeService.lovetimeDetails(true,lovetimeId));
        return webResult;
    }

    /**
     * 获取单条记录详情
     * @return
     */
    @RequestMapping("details/{id}")
    @ResponseBody
    public WebResult lovetimeDetails(
            @PathVariable("id") int lovetimeId
    ) {
        WebResult webResult = ResponseResultConfig.getResponseResult(ResponseResultConfig.ResponseResult.SUCCESS);
        webResult.setData(lovetimeService.lovetimeDetails(false,lovetimeId));
        return webResult;
    }
}
