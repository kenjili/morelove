package com.wujiuye.dao;


import com.wujiuye.pojo.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository
public interface UserDao {

    /**
     * 更新用户信息，用户名密码等不能等下
     * @param user
     * @return
     */
    int updateUser(User user);

    /**
     * 根据用户名获取用户
     * @param username
     * @return
     */
    User getUserWithUsername(String username);

    /**
     * 根据手机号码获取用户
     */
    User getUserWithPhone(String phoneNumber);

    /**
     * 获取用户所拥有的角色
     * @param username
     * @return
     */
    List<String> rolesListWithUser(String username);

    /**
     * 注册用户
     * @param user
     * @return
     */
    int registUser(User user);

    /**
     * 获取用户的登录状态信息，判断是否密码输入错误多次被禁用登录功能
     * @param user
     * @return
     */
    Map<String, Object> queryUserLoginInfo(User user);

    /**
     * 保存一条用户登录状态信息记录
     * @param userId
     * @return
     */
    int addUserLoginInfo(Integer userId);

    /**
     * 更新用户登录状态信息记录
     * @param userId    用户id
     * @param lastLoginDevice 登录设备
     * @param lastLoginDatetime 登录时间
     * @param passwordErrorCount 密码错误次数
     * @return
     */
    int updateUserLoginInfo(
            @Param("userId") Integer userId,
            @Param("lastLoginDevice") String lastLoginDevice,
            @Param("lastLoginDatetime") Date lastLoginDatetime,
            @Param("passwordErrorCount") Integer passwordErrorCount
    );
}
