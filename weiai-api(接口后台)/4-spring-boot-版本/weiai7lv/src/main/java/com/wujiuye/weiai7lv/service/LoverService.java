package com.wujiuye.weiai7lv.service;

import com.wujiuye.weiai7lv.entity.Lover;
import com.wujiuye.weiai7lv.exception.WebException;

import java.util.Map;

public interface LoverService {


    /**
     * 绑定情侣
     * @param username 对方的用户名
     * @return
     */
    Lover bindLover(String username) throws WebException;


    /**
     * 获取绑定状态
     * @return
     */
    Map<String, Object> bindState();


    /**
     * 通过对方的绑定申请
     * @return
     */
    Lover enterLover() throws WebException;

    /**
     * 获取情侣记录
     * @return
     */
    Lover findLover() throws WebException;

}
