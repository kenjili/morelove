package wjy.morelove.bean;


import java.io.Serializable;

import wjy.morelove.Jyson.JCName;
import wjy.morelove.Jyson.JFName;


/**
 * 情侣记录详情
 */
@JCName("data")
public class LoverDetails implements Serializable{

    @JFName("lover")
    private Lover lover;
    @JFName("memorialdayCount")
    private int memorialdayCount;//纪念日总数
    @JFName("movieCount")
    private int movieCount;//看过的电影记录总数
    @JFName("punchcardCount")
    private int punchcardCount;//打卡主题总数

    public Lover getLover() {
        return lover;
    }

    public void setLover(Lover lover) {
        this.lover = lover;
    }

    public int getMemorialdayCount() {
        return memorialdayCount;
    }

    public void setMemorialdayCount(int memorialdayCount) {
        this.memorialdayCount = memorialdayCount;
    }

    public int getMovieCount() {
        return movieCount;
    }

    public void setMovieCount(int movieCount) {
        this.movieCount = movieCount;
    }

    public int getPunchcardCount() {
        return punchcardCount;
    }

    public void setPunchcardCount(int punchcardCount) {
        this.punchcardCount = punchcardCount;
    }
}
