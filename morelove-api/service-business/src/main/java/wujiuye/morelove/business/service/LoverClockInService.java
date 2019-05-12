package wujiuye.morelove.business.service;

import wujiuye.morelove.pojo.CardSubject;
import wujiuye.morelove.pojo.dto.ClockInHistoryDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author wujiuye
 * @version 1.0 on 2019/5/11 {描述：}
 */
public interface LoverClockInService {

    /**
     * 打卡周期
     */
    enum CardPeriod {
        EVERY_DAY(1, "每天打卡一次"),
        EVERY_WEEK(2, "每周打卡一次"),
        EVERY_MONTH(3, "每月打卡一次");

        Integer value;
        String name;

        CardPeriod(Integer value, String name) {
            this.value = value;
            this.name = name;
        }

        public Integer getValue() {
            return this.value;
        }

        public String getName() {
            return this.name;
        }

        public static CardPeriod getCardPeriod(Integer value) {
            if (value == null) {
                return null;
            }
            switch (value) {
                case 1:
                    return EVERY_DAY;
                case 2:
                    return EVERY_WEEK;
                case 3:
                    return EVERY_MONTH;
            }
            return null;
        }
    }

    /**
     * 打卡
     */
    void clockIn(String subject);

    /**
     * 获取打卡历史，也包括对方的打卡记录
     *
     * @param subject 打卡活动主题
     */
    List<ClockInHistoryDto> clockInHistory(String subject, Integer page, Integer pageSize);

    /**
     * 创建打卡活动主题
     *
     * @param subject
     * @param cardPeriod
     */
    void createSubject(String subject, CardPeriod cardPeriod, MultipartFile multipartFile);


    /**
     * 获取我参与的所有打卡活动主题
     *
     * @return
     */
    List<CardSubject> getAllSubject();
}
