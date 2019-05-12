package wujiuye.morelove.business.service;


import wujiuye.morelove.pojo.MemorialDay;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MemorialdayService {

    void savaMemorialday(MemorialDay memorialDay, MultipartFile img);

    void delteMemorialday(int id);

    List<MemorialDay> allMemorialday();

    int getCount();

}
