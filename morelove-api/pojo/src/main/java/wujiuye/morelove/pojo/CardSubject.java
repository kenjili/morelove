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
public class CardSubject {

    private Integer id;
    private Integer loveId;
    private String subject;
    private Integer period;//打卡周期
    private String subjImg;//主题背景图片的url
    private Date createDatetime;
    private Date updateDatetime;

}
