package com.wujiuye.weiai7lv.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.wujiuye.weiai7lv.exception.WebResult;

@RequestMapping("punchCard")
@Controller
public class PunchCardController{


    /**
     * 添加打卡
     * @param punchCardTitle 打卡主题
     * @param period    周期
     * @return
     */
    @RequestMapping("add")
    @ResponseBody
    public WebResult addPunchCard(
            @RequestParam("title") String punchCardTitle,
            int period
    ){
        return null;
    }


    /**
     * 打卡记录
     * @param role 角色，0获取自己的，1获取对方的
     * @param year 年份
     * @param month 月份
     * @return
     */
    @RequestMapping("record/{role}/{year}/{month}")
    @ResponseBody
    public WebResult otherPuchCardRecord(
            @PathVariable("role") int role,
            @PathVariable("year") int year,
            @PathVariable("month") int month
    ){
        return null;
    }

    /**
     * 获取打卡主题列表、并获取当前期是否已经打卡
     * @return
     */
    @ResponseBody
    @RequestMapping("list")
    public WebResult listPunchCard(){
        return null;
    }
}
