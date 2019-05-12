package wjy.morelove.bean;

import java.io.Serializable;

import wjy.morelove.Jyson.JFName;

/**
 * 卡片，主题打卡
 */
public class SubjectCard implements Serializable {

    @JFName("subject")
    private String subject;//主题
    @JFName("subjImg")
    private String subjImg;
    @JFName("period")
    private Integer period;
    @JFName("isFinish")
    private boolean isFinish;//是否已经完成打卡

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getSubjImg() {
        return subjImg;
    }

    public void setSubjImg(String subjImg) {
        this.subjImg = subjImg;
    }

    public Integer getPeriod() {
        return period;
    }

    public void setPeriod(Integer period) {
        this.period = period;
    }

    public boolean isFinish() {
        return isFinish;
    }

    public void setFinish(boolean finish) {
        isFinish = finish;
    }
}
