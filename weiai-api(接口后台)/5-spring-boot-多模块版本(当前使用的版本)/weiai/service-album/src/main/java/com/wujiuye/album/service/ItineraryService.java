package com.wujiuye.album.service;


import com.wujiuye.pojo.Album;
import com.wujiuye.pojo.Itinerary;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ItineraryService {

    /**
     * 发表旅行日记相册
     * @param itinerary
     * @param album
     * @param photoFiles
     * @return
     */
    Itinerary publicItinerary(Itinerary itinerary, Album album, MultipartFile[] photoFiles);

    /**
     * 分页列表
     * @param pageSize
     * @param currentPage
     * @return
     */
    List<Itinerary> listItinerary(int pageSize, int currentPage);

    /**
     * 获取旅行记详情
     * @param id
     * @return
     */
    Itinerary queryItinerary(int id);
}
