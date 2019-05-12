package wjy.morelove.bean;

import java.io.Serializable;

/**
 * 电影票房
 *
 * @author wjy
 */

public class MovieBoxOffice implements Serializable {

    private String movieName;//电影名称
    private String movieImg;//电影图片
    private float boxOffice;//实时票房、单日票房、单月票房
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

    public int getMovieDay() {
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
        return releaseTime;
    }

    public void setReleaseTime(String releaseTime) {
        this.releaseTime = releaseTime;
    }
}
