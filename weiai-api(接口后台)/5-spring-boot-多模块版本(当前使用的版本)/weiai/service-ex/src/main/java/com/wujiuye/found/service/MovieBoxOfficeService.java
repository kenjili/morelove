package com.wujiuye.found.service;

import com.wujiuye.pojo.MovieBoxOffice;

import java.util.List;

public interface MovieBoxOfficeService {

    /**
     * 查询实时票房
     * @return
     */
    List<MovieBoxOffice> queryHourBoxOffice(int year, int moth, int day, int hour);


    /**
     * 查询单日票房
     * @return
     */
    List<MovieBoxOffice> queryDayBoxOffice(int year, int month, int day);


    /**
     * 单月票房
     * @return
     */
    List<MovieBoxOffice> queryMonthBoxOffice(int year, int month);
}
