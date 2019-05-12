package wujiuye.morelove.service;

import wujiuye.morelove.pojo.Lover;

import java.util.Map;

public interface LoverService {


    /**
     * 绑定情侣
     *
     * @param username 对方的用户名
     * @return
     */
    Lover bindLover(String username);


    /**
     * 获取绑定状态
     *
     * @return
     */
    Map<String, Object> bindState();


    /**
     * 通过对方的绑定申请
     *
     * @return
     */
    Lover enterLover();

    /**
     * 获取情侣记录
     *
     * @return
     */
    Lover findLover();

}
