package wujiuye.morelove.controller;


import wujiuye.morelove.business.service.LoverClockInService;
import wujiuye.morelove.business.service.MemorialdayService;
import wujiuye.morelove.common.exception.ResponseResultConfig;
import wujiuye.morelove.common.exception.WebException;
import wujiuye.morelove.common.exception.WebResult;
import wujiuye.morelove.pojo.Lover;
import wujiuye.morelove.service.LoverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;


@Controller
@RequestMapping("lover")
public class LoverController {


    @Autowired
    private LoverService loverService;
    @Autowired
    private MemorialdayService memorialdayService;
    @Autowired
    private LoverClockInService loverClockInService;

    /**
     * 申请与对方绑定为情侣
     * 只有绑定成功后才会拥有情侣权限
     *
     * @return
     */
    @RequestMapping(value = "bindLover", method = RequestMethod.POST)
    @ResponseBody
    public WebResult requestBindLover(
            //对方用户名
            @RequestParam("otherUsername") String username
    ) throws WebException {
        Lover lover = this.loverService.bindLover(username);
        WebResult webResult = ResponseResultConfig.getResponseResult(ResponseResultConfig.ResponseResult.SUCCESS);
        webResult.setData(lover);
        return webResult;
    }


    /**
     * 查询绑定状态
     *
     * @return
     */
    @RequestMapping("bindState")
    @ResponseBody
    public WebResult queryBindState() {
        WebResult webResult = ResponseResultConfig.getResponseResult(ResponseResultConfig.ResponseResult.SUCCESS);
        webResult.setData(loverService.bindState());
        return webResult;
    }


    /**
     * 确认绑定
     *
     * @return
     */
    @RequestMapping("enterBind")
    @ResponseBody
    public WebResult enterBind() throws WebException {
        Lover lover = this.loverService.enterLover();
        WebResult webResult = ResponseResultConfig.getResponseResult(ResponseResultConfig.ResponseResult.SUCCESS);
        webResult.setData(lover);
        return webResult;
    }


    /**
     * 获取情侣记录
     * 需要情侣权限
     *
     * @return
     */
    @RequestMapping("record")
    @ResponseBody
    public WebResult getLoverInfo() throws WebException {
        Map<String, Object> data = new HashMap<>();
        Lover lover = this.loverService.findLover();
        data.put("lover", lover);
        data.put("memorialdayCount", memorialdayService.getCount());//纪念日总数
        data.put("punchcardCount", loverClockInService.getAllSubject().size());//打卡主题总数
        WebResult webResult = ResponseResultConfig.getResponseResult(ResponseResultConfig.ResponseResult.SUCCESS);
        webResult.setData(data);
        return webResult;
    }

}
