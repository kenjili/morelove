package wujiuye.morelove.pojo.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author wujiuye
 * @version 1.0 on 2019/5/11 {描述：}
 */
@NoArgsConstructor
@Data
public class ClockInHistoryDto {

    private Integer loveId;
    private UserDto user;
    private SubjectDto subject;
    private Date ciDate;//打卡时间

    @NoArgsConstructor
    @Data
    public static class UserDto {
        private Integer id;
        private String username;
        private String headThumb;
        private int sex;//0为男，1为女
    }

    @NoArgsConstructor
    @Data
    public static class SubjectDto {
        private Integer subjectId;//打卡主题id
        private String name;//主题名称
    }
}
