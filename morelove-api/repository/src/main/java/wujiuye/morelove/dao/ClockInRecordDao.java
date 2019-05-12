package wujiuye.morelove.dao;

import wujiuye.morelove.pojo.ClockInRecord;
import wujiuye.morelove.pojo.dto.ClockInHistoryDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author wujiuye
 * @version 1.0 on 2019/5/11 {描述：}
 */
public interface ClockInRecordDao {

    ClockInRecord selectByDate(@Param("subjectId") Integer subjectId,@Param("userId")Integer userId,@Param("fromDate")String fromDate,@Param("toDate")String toDate);

    /**
     * 打卡
     *
     * @param record
     * @return
     */
    Integer clockIn(ClockInRecord record);

    List<ClockInHistoryDto> findClockInHitory(@Param("loveId") Integer loveId, @Param("subject") String subject, @Param("offerset") Integer offerset, @Param("limit") Integer limit);
}
