package com.wujiuye.weiai7lv.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import com.wujiuye.weiai7lv.entity.MemorialDay;
import com.wujiuye.weiai7lv.exception.ResponseResultConfig;
import com.wujiuye.weiai7lv.exception.WebException;
import com.wujiuye.weiai7lv.exception.WebResult;
import com.wujiuye.weiai7lv.service.MemorialdayService;

import java.text.ParseException;
import java.text.SimpleDateFormat;

@RequestMapping("memorialDay")
@Controller
public class MemorialDayController {

    @Autowired
    private MemorialdayService memorialdayService;

    /**
     * 添加纪念日
     *
     * @param priority 优先级，重要性 ,纪念日会根据这个排序
     * @param name     纪念日名称
     * @param date     日期 yyyy-MM-dd
     * @param img      背景图片
     * @return
     */
    @RequestMapping(value = "add",method = RequestMethod.POST)
    @ResponseBody
    public WebResult addMemorialDay(
            @RequestParam("priority") int priority,
            @RequestParam("name") String name,
            @RequestParam("date") String date,
            @RequestParam("image") MultipartFile[] img
    ) throws WebException {
        MemorialDay memorialDay = new MemorialDay();
        memorialDay.setMemorialName(name);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            memorialDay.setMemorialDate(sdf.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        memorialDay.setPriority(priority);
        memorialdayService.savaMemorialday(memorialDay,img[0]);
        return ResponseResultConfig.getResponseResult(ResponseResultConfig.ResponseResult.SUCCESS);
    }

    /**
     * 纪念日列表,会返回详情
     *
     * @return
     */
    @RequestMapping("list")
    @ResponseBody
    public WebResult listMemorialDay() {
        WebResult webResult = ResponseResultConfig.getResponseResult(ResponseResultConfig.ResponseResult.SUCCESS);
        webResult.setData(memorialdayService.allMemorialday());
        return webResult;
    }


    /**
     * 删除纪念日
     *
     * @param mdId
     * @return
     */
    @RequestMapping(value = "delete",method = RequestMethod.POST)
    @ResponseBody
    public WebResult deleteMemorialDay(
            @RequestParam("id") int mdId
    ) {
        memorialdayService.delteMemorialday(mdId);
        WebResult webResult = ResponseResultConfig.getResponseResult(ResponseResultConfig.ResponseResult.SUCCESS);
        return webResult;
    }

}
