package wujiuye.morelove.chat.srv.service.impl;

import io.netty.util.internal.StringUtil;
import org.springframework.stereotype.Service;
import wujiuye.morelove.chat.packet.bean.User;
import wujiuye.morelove.chat.srv.dao.UserDao;
import wujiuye.morelove.chat.srv.service.UserService;

import javax.annotation.Resource;


@Service
public class UserServiceImpl implements UserService {

    @Resource
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
