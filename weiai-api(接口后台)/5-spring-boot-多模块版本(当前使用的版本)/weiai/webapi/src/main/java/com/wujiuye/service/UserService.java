package com.wujiuye.service;

import com.wujiuye.pojo.User;

import java.util.List;
import java.util.Map;

public interface UserService {


    /**
     * 用户退出登陆
     *
     * @return
     */
    void userLogout(User user);

    /**
     * 根据用户名查询用户
     *
     * @param username
     * @return
     */
    User getUserWithUsername(String username);

    /**
     * 获取用户所拥有的角色
     *
     * @param username
     * @return
     */
    List<String> rolesListWithUser(String username);


    /**
     * 注册用户
     *
     * @return 用户信息，密码等信息为空
     */
    User registUser(User user);

    /**
     * 获取用户的登录状态信息，判断是否密码输入错误多次被禁用登录功能
     *
     * @param user
     * @return
     */
    Map<String, Object> queryUserLoginInfo(User user);


    /**
     * 保存一条用户登录状态信息记录
     *
     * @param user
     * @return
     */
    int addUserLoginInfo(User user);

    /**
     * 更新用户登录状态信息记录
     *
     * @param user
     * @param lastLoginDevice    登录设备
     * @param passwordErrorCount 密码错误次数
     * @return
     */
    int updateUserLoginInfo(
            User user,
            String lastLoginDevice,
            Integer passwordErrorCount
    );
}
