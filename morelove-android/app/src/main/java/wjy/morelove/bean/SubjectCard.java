package wjy.morelove.bean;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 卡片，主题打卡
 */
public class SubjectCard implements Serializable {

    private String subject;//主题
    private String note;//打卡说明
    //当前周期的打卡始止时间
    private LocalDateTime startDatetime;
    private LocalDateTime endDatetime;
    private boolean isFinish;//是否已经完成打卡
    private String imgUrl;//卡片图片背景

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public LocalDateTime getStartDatetime() {
        return startDatetime;
    }

    public void setStartDatetime(LocalDateTime startDatetime) {
        this.startDatetime = startDatetime;
    }

    public LocalDateTime getEndDatetime() {
        return endDatetime;
    }

    public void setEndDatetime(LocalDateTime endDatetime) {
        this.endDatetime = endDatetime;
    }

    public Boolean getFinish() {
        return isFinish;
    }

    public void setFinish(Boolean finish) {
        isFinish = finish;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
