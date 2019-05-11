package com.wujiuye.weiai7lv.service.impl;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import com.wujiuye.weiai7lv.entity.MovieBoxOffice;
import com.wujiuye.weiai7lv.service.MovieBoxOfficeService;
import com.wujiuye.weiai7lv.utils.DateTimeUtils;
import com.wujiuye.weiai7lv.utils.showapi.MyShowApiRequest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 电影票房
 * @author wjy
 */
@Service
public class MovieBoxOfficeServiceImpl implements MovieBoxOfficeService {

    private static String MOVIE_URL = "http://www.cbooo.cn";
    private static String MOVIE_HOUR_URL = MOVIE_URL + "/BoxOffice/GetHourBoxOffice";
    private static String MOVIE_DAY_URL = MOVIE_URL + "/BoxOffice/GetDayBoxOffice";
    private static String MOVIE_MONTH_URL = MOVIE_URL + "/BoxOffice/getMonthBox";

    @Cacheable(cacheNames = "movieBoxOffice",key = "'hour_'+#year+#month+#day+#hour")
    @Override
    public List<MovieBoxOffice> queryHourBoxOffice(int year,int moth,int day,int hour) {
        List<MovieBoxOffice> movieBoxOffices = new ArrayList<>();
        //结果图片需要加上域名http://www.cbooo.cn/moviepic/
        String result = new MyShowApiRequest(MOVIE_HOUR_URL)
                .addTextPara("d",
                        DateTimeUtils.stringtime2Longdatetime(""+year+"-"+(moth>9?moth:"0"+moth)+"-"+(day>9?day:"0"+day)
                                +" "+(hour>9?hour:"0"+hour)+":00:00")+"")
                .get();
        if (result == null || result.trim().equals("")) return movieBoxOffices;
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = null;
        try {
            rootNode = mapper.readTree(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (rootNode == null) return movieBoxOffices;
        JsonNode data2 = rootNode.path("data2");
        for(int i=0;i<data2.size();i++){
            JsonNode item = data2.get(i);
            MovieBoxOffice movieBoxOffice = new MovieBoxOffice();
            movieBoxOffice.setMovieName(item.path("MovieName").asText());
            movieBoxOffice.setMovieImg(MOVIE_URL+"/moviepic/"+item.path("MovieImg").asText());
            movieBoxOffice.setMovieDay(item.path("movieDay").asInt());
//            movieBoxOffice.setBoxPer((float) item.path("boxPer").asDouble());
            movieBoxOffice.setBoxOffice((float) item.path("BoxOffice").asDouble());
            movieBoxOffices.add(movieBoxOffice);
        }
        return movieBoxOffices;
    }


    @Cacheable(cacheNames = "movieBoxOffice",key = "'day_'+#year+#month+#day")
    @Override
    public List<MovieBoxOffice> queryDayBoxOffice(int year,int month,int day) {
        List<MovieBoxOffice> movieBoxOffices = new ArrayList<>();
        //结果图片需要加上域名http://www.cbooo.cn
        String result = new MyShowApiRequest(MOVIE_DAY_URL)
                .addTextPara("d", DateTimeUtils.stringtime2Longdatetime(""+year+"-"+(month>9?month:"0"+month)+"-"+(day>9?day:"0"+day)
                        +" 01:00:00")+"" + "")
                .get();
        if (result == null || result.trim().equals("")) return movieBoxOffices;
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = null;
        try {
            rootNode = mapper.readTree(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (rootNode == null) return movieBoxOffices;
        JsonNode data1 = rootNode.path("data1");
        for(int i=0;i<data1.size();i++){
            JsonNode item = data1.get(i);
            MovieBoxOffice movieBoxOffice = new MovieBoxOffice();
            movieBoxOffice.setMovieName(item.path("MovieName").asText());
            movieBoxOffice.setMovieImg(MOVIE_URL+item.path("MovieImg").asText());
            movieBoxOffice.setMovieDay(item.path("MovieDay").asInt());
//            movieBoxOffice.setBoxPer((float) item.path("AvgPrice").asDouble());
            movieBoxOffice.setBoxOffice((float) item.path("BoxOffice").asDouble());
            movieBoxOffices.add(movieBoxOffice);
        }
        return movieBoxOffices;
    }


    @Cacheable(cacheNames = "movieBoxOffice",key = "'month_'+#year+#month")
    @Override
    public List<MovieBoxOffice> queryMonthBoxOffice(int year, int month) {
        List<MovieBoxOffice> movieBoxOffices = new ArrayList<>();
        String result = new MyShowApiRequest(MOVIE_MONTH_URL)
                .addTextPara("sdate",year+"-"+(month>9?month:"0"+month)+"-01")
                .get();
        if (result == null || result.trim().equals("")) return movieBoxOffices;
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = null;
        try {
            rootNode = mapper.readTree(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (rootNode == null) return movieBoxOffices;
        JsonNode data1 = rootNode.path("data1");
        for(int i=0;i<data1.size();i++){
            JsonNode item = data1.get(i);
            MovieBoxOffice movieBoxOffice = new MovieBoxOffice();
            movieBoxOffice.setMovieName(item.path("MovieName").asText());
            movieBoxOffice.setMovieImg(item.path("defaultImage").asText());
            movieBoxOffice.setReleaseTime(item.path("releaseTime").asText());
//            movieBoxOffice.setBoxPer((float) item.path("avgboxoffice").asDouble());//平均票价
            movieBoxOffice.setBoxOffice((float) item.path("boxoffice").asDouble());//单月票房（单位万）
            movieBoxOffices.add(movieBoxOffice);
        }
        return movieBoxOffices;
    }
}
