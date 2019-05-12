package wujiuye.morelove.found.service;

import wujiuye.morelove.pojo.MovieBoxOffice;

import java.util.List;
import java.util.concurrent.Future;

public interface MovieBoxOfficeAsyncService {

    /**
     * 查询实时票房
     * @return
     */
    Future<List<MovieBoxOffice>> hourBoxOfficeList(int year, int moth, int day, int hour);


    /**
     * 查询单日票房
     * @return
     */
    Future<List<MovieBoxOffice>> dayBoxOfficeList(int year, int month, int day);


    /**
     * 单月票房
     * @return
     */
    Future<List<MovieBoxOffice>> monthBoxOfficeList(int year, int month);

}
