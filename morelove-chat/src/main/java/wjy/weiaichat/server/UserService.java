package wjy.weiaichat.server;

import wjy.weiaichat.session.User;

public interface UserService {


    boolean validationRegist(User user);

    User queryUser(String username);

}
