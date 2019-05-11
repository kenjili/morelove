package com.wujiuye.weiai7lv.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.wujiuye.weiai7lv.exception.ResponseResultConfig;
import com.wujiuye.weiai7lv.exception.WebResult;

@Controller
@RequestMapping("/")
public class IndexController {

    /**
     * 获取首页数据
     * @return
     */
    @RequestMapping("index")
    @ResponseBody
    public WebResult index(){
        return ResponseResultConfig.getResponseResult(ResponseResultConfig.ResponseResult.SUCCESS);
    }

}

