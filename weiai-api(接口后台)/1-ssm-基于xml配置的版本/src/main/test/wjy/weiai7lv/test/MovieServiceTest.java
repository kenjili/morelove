package wjy.weiai7lv.test;


import org.junit.Test;
import wjy.weiai7lv.utils.showapi.MyShowApiRequest;

public class MovieServiceTest {


    /**
     * @by wujiuye
     * 打开http://www.cbooo.cn/realtime
     * 开发者工具->网络->过滤掉静态文件之后发现的,经猜测参数d传的是当前时间戳,精确到毫秒的时间戳
     * GetHourBoxOffice=》时时票房
     * GetDayBoxOffice=》当日票房
     * <p>
     * 当周有些区别
     * getWeekInfoData?sdate=2018-09-24
     * sdate传的是上周的日期（当前周的上周周一，因为当前周还没结束，所以不可以统计当前周的）
     * <p>
     * 当月票房：getMonthBox?sdate=2018-10-01
     * sdate为当月的1号的日期
     * <p>
     * 其它更多自己去看了
     */
    private static String MOVIE_URL = "http://www.cbooo.cn/BoxOffice";
    private static String MOVIE_HOUR_URL = MOVIE_URL + "/GetHourBoxOffice";
    private static String MOVIE_DAY_URL = MOVIE_URL + "/GetDayBoxOffice";
    private static String MOVIE_WEEK_URL = MOVIE_URL + "/getWeekInfoData";
    private static String MOVIE_MONTH_URL = MOVIE_URL + "/getMonthBox";

    @Test
    public void testHour() {
        //结果图片需要加上域名http://www.cbooo.cn
        String string = new MyShowApiRequest(MOVIE_HOUR_URL)
                .addTextPara("d", System.currentTimeMillis() + "")
                .get();
        System.out.println(string);
    }

    @Test
    public void testDay() {
        //结果图片需要加上域名"http://www.cbooo.cn"
        String string = new MyShowApiRequest(MOVIE_DAY_URL)
                .addTextPara("d", System.currentTimeMillis() + "")
                .get();
        System.out.println(string);
    }

    @Test
    public void testWeek() {
        String string = new MyShowApiRequest(MOVIE_WEEK_URL)
                .addTextPara("sdate","2018-09-24")
                .get();
        System.out.println(string);
    }

    @Test
    public void testMonth() {
        String string = new MyShowApiRequest(MOVIE_MONTH_URL)
                .addTextPara("sdate","2018-10-01")
                .get();
        System.out.println(string);
    }
}
