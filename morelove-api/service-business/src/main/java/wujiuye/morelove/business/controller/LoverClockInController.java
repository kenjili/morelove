package wujiuye.morelove.business.controller;

import org.springframework.beans.factory.annotation.Autowired;
import wujiuye.morelove.business.service.LoverClockInService;
import wujiuye.morelove.common.exception.ResponseResultConfig;
import wujiuye.morelove.common.exception.WebResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author wujiuye
 * @version 1.0 on 2019/5/11 {描述：}
 */
@RequestMapping("/subject")
@RestController
public class LoverClockInController {

    @Autowired
    private LoverClockInService loverClockInService;

    /**
     * 创建打卡主题
     *
     * @return
     */
    @PostMapping("/create")
    public WebResult createSubject(@RequestParam("subject") String subject,
                                   @RequestParam("period") Integer cardPeriod,
                                   @RequestParam("subjectImg") MultipartFile multipartFile) {
        loverClockInService.createSubject(subject, LoverClockInService.CardPeriod.getCardPeriod(cardPeriod), multipartFile);
        WebResult webResult = ResponseResultConfig.getResponseResult(ResponseResultConfig.ResponseResult.SUCCESS);
        webResult.setErrorMessage("创建成功！");
        return webResult;
    }


    /**
     * 获取所有打卡活动主题
     *
     * @return
     */
    @GetMapping("/list")
    public WebResult subjectList() {
        Object data = loverClockInService.getAllSubject();
        WebResult webResult = ResponseResultConfig.getResponseResult(ResponseResultConfig.ResponseResult.SUCCESS);
        webResult.setData(data);
        return webResult;
    }


    /**
     * 打卡
     *
     * @param subject
     * @return
     */
    @PostMapping("/clockin")
    public WebResult clockIn(@RequestParam("subject") String subject) {
        loverClockInService.clockIn(subject);
        return ResponseResultConfig.getResponseResult(ResponseResultConfig.ResponseResult.SUCCESS);
    }

    /**
     * 查看打卡记录
     *
     * @return
     */
    @GetMapping("/clockin/history/{page}/{pageSize}")
    public WebResult clockInRecord(
            @RequestParam("subject") String subject,
            @PathVariable("page") Integer page,
            @PathVariable("pageSize") Integer pageSize
    ) {
        Object data = loverClockInService.clockInHistory(subject, page, pageSize);
        WebResult webResult = ResponseResultConfig.getResponseResult(ResponseResultConfig.ResponseResult.SUCCESS);
        webResult.setData(data);
        return webResult;
    }
}
