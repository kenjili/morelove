package wujiuye.morelove.chat.srv.service;


import wujiuye.morelove.chat.packet.bean.User;

public interface UserService {


    boolean validationRegist(User user);

    User queryUser(String username);

}
