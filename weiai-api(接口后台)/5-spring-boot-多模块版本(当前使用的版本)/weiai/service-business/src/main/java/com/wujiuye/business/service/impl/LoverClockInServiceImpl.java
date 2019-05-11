package com.wujiuye.business.service.impl;

import com.wujiuye.business.service.LoverClockInService;
import com.wujiuye.common.config.WebConstantConfig;
import com.wujiuye.common.exception.WebException;
import com.wujiuye.common.utils.DateTimeUtils;
import com.wujiuye.common.utils.StringUtils;
import com.wujiuye.common.utils.UploadImageFileUtils;
import com.wujiuye.dao.CardSubjectDao;
import com.wujiuye.dao.ClockInRecordDao;
import com.wujiuye.dao.LoverDao;
import com.wujiuye.dao.UserDao;
import com.wujiuye.pojo.CardSubject;
import com.wujiuye.pojo.ClockInRecord;
import com.wujiuye.pojo.Lover;
import com.wujiuye.pojo.User;
import com.wujiuye.pojo.dto.ClockInHistoryDto;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author wujiuye
 * @version 1.0 on 2019/5/11 {描述：}
 */
@Service
public class LoverClockInServiceImpl implements LoverClockInService {

    @Resource
    private UserDao userDao;
    @Resource
    private LoverDao loverDao;
    @Resource
    private CardSubjectDao cardSubjectDao;
    @Resource
    private ClockInRecordDao clockInRecordDao;
    @Autowired
    private WebConstantConfig webConstantConfig;

    private User getLoginUser() {
        //获取通过shiro认证的用户信息
        String username = (String) SecurityUtils.getSubject().getPrincipal();
        User user = userDao.getUserWithUsername(username);
        return user;
    }

    private Lover getLoverByUser(User user) {
        Lover lover = loverDao.findLover(user.getId());
        return lover;
    }

    @Override
    public void clockIn(String subject) {
        User user = getLoginUser();
        Lover lover = getLoverByUser(user);
        CardSubject cardSubject = cardSubjectDao.findOne(lover.getId(), subject);
        if (cardSubject == null) {
            throw new WebException("主题不存在！");
        }
        CardPeriod cardPeriod = CardPeriod.getCardPeriod(cardSubject.getPeriod());
        String fromDate = null, toDate = null;
        switch (cardPeriod) {
            case EVERY_DAY:
                //判断今天是否已经打卡了
                fromDate = DateTimeUtils.getCurrentYMDString() + " 00:00:00";
                toDate = DateTimeUtils.getAfterDaysDayYMDStringWithDays(1) + " 00:00:00";
                break;
            case EVERY_WEEK:
                //判断本周是否打卡了
                break;
            case EVERY_MONTH:
                //判断本月是否已经打卡了
                break;
        }
        ClockInRecord clockInRecord = clockInRecordDao.selectByDate(cardSubject.getId(), user.getId(), fromDate, toDate);
        if (clockInRecord != null) {
            throw new WebException("已经打过卡了！");
        }
        clockInRecord = new ClockInRecord();
        clockInRecord.setSubjectId(cardSubject.getId());
        clockInRecord.setUserId(user.getId());
        clockInRecordDao.clockIn(clockInRecord);
    }

    @Override
    public List<ClockInHistoryDto> clockInHistory(String subject, Integer page, Integer pageSize) {
        User user = getLoginUser();
        Lover lover = getLoverByUser(user);
        if (page == null || page < 1) {
            page = 1;
        }
        if (pageSize < 1) {
            pageSize = 1;
        }
        if (pageSize > 100) {
            pageSize = 100;
        }
        List<ClockInHistoryDto> result = clockInRecordDao.findClockInHitory(lover.getId(), subject, (page - 1) * pageSize, pageSize);
        return result;
    }

    @Override
    public void createSubject(String subject, CardPeriod cardPeriod, MultipartFile multipartFile) {
        if (StringUtils.strIsNull(subject)) {
            throw new WebException("主题不能为空！");
        }
        if (cardPeriod == null) {
            throw new WebException("周期不能为空！");
        }
        User user = getLoginUser();
        Lover lover = getLoverByUser(user);
        CardSubject cardSubject = new CardSubject();
        cardSubject.setSubject(subject);
        cardSubject.setPeriod(cardPeriod.getValue());
        cardSubject.setLoveId(lover.getId());
        if (multipartFile != null) {
            String savaPath = "/lover-" + lover.getId() + "-" + subject;
            //放在纪念日目录下吧
            String[] paths = UploadImageFileUtils.savaImageFile(multipartFile, webConstantConfig.getPrivateUploadFileRootPath(), webConstantConfig.getMemorialDayImagesRootPath() + savaPath);
            cardSubject.setSubjImg(paths[0]);
        }
        cardSubjectDao.insertOne(cardSubject);
    }

    @Override
    public List<CardSubject> getAllSubject() {
        User user = getLoginUser();
        Lover lover = getLoverByUser(user);
        List<CardSubject> result = cardSubjectDao.getAllSubject(lover.getId());
        if (result != null && result.size() > 0) {
            for (CardSubject cardSubject : result) {
                if (!StringUtils.strIsNull(cardSubject.getSubjImg())) {
                    cardSubject.setSubjImg(webConstantConfig.getPrivateResourceServerName() + cardSubject.getSubjImg());
                }
            }
        }
        return result;
    }

}
