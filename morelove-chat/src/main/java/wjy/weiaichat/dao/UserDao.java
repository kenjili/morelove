package wjy.weiaichat.dao;

import wjy.weiaichat.session.User;

public interface UserDao {

    User getUserByUsername(String username);

}
