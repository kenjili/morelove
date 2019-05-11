package com.wujiuye.ui.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 首页-测试接口页
 *
 * @author wjy
 */
@Controller
@RequestMapping
public class IndexController {

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("pageTitle", "暂不支持网页端，请下载手机APP！");
        return "index/index";
    }

}
