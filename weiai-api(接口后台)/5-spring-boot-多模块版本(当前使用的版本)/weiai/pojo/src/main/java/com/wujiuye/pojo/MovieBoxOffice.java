package com.wujiuye.pojo;


import com.wujiuye.common.utils.DateTimeUtils;
import com.wujiuye.common.utils.StringUtils;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 电影票房,不存数据库，存缓存
 * @author wjy
 */
public class MovieBoxOffice implements Serializable {

    @Getter
    @Setter
    private String movieName;//电影名称
    @Getter
    @Setter
    private String movieImg;//电影图片
    @Getter
    @Setter
    private float boxOffice;//实时票房、单日票房、单月票房
    //private float sumBoxOffice;//累计票房（只有当日、当月接口有）
    @Setter
    private int movieDay;//上映天数
    @Setter
    private String releaseTime;//上映日期(上映日期和上映天数可通过知其一计算另一个)

    public int getMovieDay() {
        if(this.movieDay==0){
            if(!StringUtils.strIsNull(this.releaseTime)){
                //根据上映日期计算天数
                this.movieDay = DateTimeUtils.datetimeOrDatetimeDays(System.currentTimeMillis(),DateTimeUtils.stringtime2Longdatetime(this.releaseTime+" 01:00:00"));
            }
        }
        return movieDay;
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

}
