package wjy.weiai7lv.service;

import org.springframework.web.multipart.MultipartFile;
import wjy.weiai7lv.entity.MemorialDay;
import wjy.weiai7lv.exception.WebException;

import java.util.List;

public interface MemorialdayService {

    void savaMemorialday(MemorialDay memorialDay, MultipartFile img) throws WebException;

    void delteMemorialday(int id);

    List<MemorialDay> allMemorialday();

    int getCount();

}
