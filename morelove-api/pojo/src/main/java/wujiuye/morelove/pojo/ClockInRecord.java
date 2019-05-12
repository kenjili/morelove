package wujiuye.morelove.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author wujiuye
 * @version 1.0 on 2019/5/11 {描述：}
 */
@NoArgsConstructor
@Data
public class ClockInRecord {

    private Integer id;
    private Integer subjectId;//打卡主题id
    private Integer userId;//打卡用户
    private Date createDatetime;
    private Date updateDatetime;
}
