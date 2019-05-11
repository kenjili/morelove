package wjy.weiai.bean;

import java.util.ArrayList;
import java.util.List;

import wjy.weiai.Jyson.JCName;
import wjy.weiai.Jyson.JFName;


@JCName("data")
public class BoxOfficeData {

    @JFName("hourBoxOffice")
    private List<MovieBoxOffice> hourBoxOffice = new ArrayList<>();
    @JFName("dayBoxOffice")
    private List<MovieBoxOffice> dayBoxOffice = new ArrayList<>();
    @JFName("monthBoxOffice")
    private List<MovieBoxOffice> monthBoxOffice = new ArrayList<>();

    public List<MovieBoxOffice> getHourBoxOffice() {
        return hourBoxOffice;
    }

    public void setHourBoxOffice(List<MovieBoxOffice> hourBoxOffice) {
        this.hourBoxOffice = hourBoxOffice;
    }

    public List<MovieBoxOffice> getDayBoxOffice() {
        return dayBoxOffice;
    }

    public void setDayBoxOffice(List<MovieBoxOffice> dayBoxOffice) {
        this.dayBoxOffice = dayBoxOffice;
    }

    public List<MovieBoxOffice> getMonthBoxOffice() {
        return monthBoxOffice;
    }

    public void setMonthBoxOffice(List<MovieBoxOffice> monthBoxOffice) {
        this.monthBoxOffice = monthBoxOffice;
    }
}
