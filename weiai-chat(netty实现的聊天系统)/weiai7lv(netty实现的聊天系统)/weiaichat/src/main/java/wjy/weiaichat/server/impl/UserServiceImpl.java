package wjy.weiaichat.server.impl;

import io.netty.util.internal.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wjy.weiaichat.dao.UserDao;
import wjy.weiaichat.server.UserService;
import wjy.weiaichat.session.User;


@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public boolean validationRegist(User user) {
        User realUser = userDao.getUserByUsername(user.getUsername());
        if(realUser==null)
            return false;
        if(!realUser.getUsername().equals(user.getUsername()))
            return false;
        return true;
    }

    @Override
    public User queryUser(String username) {
        if(StringUtil.isNullOrEmpty(username))return null;
        User user = userDao.getUserByUsername(username);
        return user;
    }
}
