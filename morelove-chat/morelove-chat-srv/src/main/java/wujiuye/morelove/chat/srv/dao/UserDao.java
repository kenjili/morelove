package wujiuye.morelove.chat.srv.dao;


import wujiuye.morelove.chat.packet.bean.User;

public interface UserDao {

    User getUserByUsername(String username);

}
