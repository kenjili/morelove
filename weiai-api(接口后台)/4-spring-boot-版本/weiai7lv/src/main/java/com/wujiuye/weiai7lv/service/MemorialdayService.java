package com.wujiuye.weiai7lv.service;

import com.wujiuye.weiai7lv.entity.MemorialDay;
import com.wujiuye.weiai7lv.exception.WebException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MemorialdayService {

    void savaMemorialday(MemorialDay memorialDay, MultipartFile img) throws WebException;

    void delteMemorialday(int id);

    List<MemorialDay> allMemorialday();

    int getCount();

}
