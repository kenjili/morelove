package wjy.weiai7lv.entity;


import wjy.weiai7lv.utils.DateTimeUtils;
import wjy.weiai7lv.utils.StringUtils;

import java.io.Serializable;

/**
 * 电影票房,不存数据库，存缓存
 * @author wjy
 */
public class MovieBoxOffice implements Serializable {

    private String movieName;//电影名称
    private String movieImg;//电影图片
    private float boxOffice;//实时票房、单日票房、单月票房
    //private float sumBoxOffice;//累计票房（只有当日、当月接口有）
    private int movieDay;//上映天数
    private float boxPer;//平均票价
    private String releaseTime;//上映日期(上映日期和上映天数可通过知其一计算另一个)

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public String getMovieImg() {
        return movieImg;
    }

    public void setMovieImg(String movieImg) {
        this.movieImg = movieImg;
    }

    public float getBoxOffice() {
        return boxOffice;
    }

    public void setBoxOffice(float boxOffice) {
        this.boxOffice = boxOffice;
    }

//    public float getSumBoxOffice() {
//        return sumBoxOffice;
//    }
//
//    public void setSumBoxOffice(float sumBoxOffice) {
//        this.sumBoxOffice = sumBoxOffice;
//    }

    public int getMovieDay() {
        if(this.movieDay==0){
            if(!StringUtils.strIsNull(this.releaseTime)){
                //根据上映日期计算天数
                this.movieDay = DateTimeUtils.datetimeOrDatetimeDays(System.currentTimeMillis(),DateTimeUtils.stringtime2Longdatetime(this.releaseTime+" 01:00:00"));
            }
        }
        return movieDay;
    }

    public void setMovieDay(int movieDay) {
        this.movieDay = movieDay;
    }

    public float getBoxPer() {
        return boxPer;
    }

    public void setBoxPer(float boxPer) {
        this.boxPer = boxPer;
    }

    public String getReleaseTime() {
        if(StringUtils.strIsNull(this.releaseTime)){
            if(this.movieDay>0){
                //根据上映天数和当前系统日期计算上映日期
                this.releaseTime = DateTimeUtils.getBeforDayYMDStringWithDays(this.movieDay);
            }
        }
        return releaseTime;
    }

    public void setReleaseTime(String releaseTime) {
        this.releaseTime = releaseTime;
    }
}
