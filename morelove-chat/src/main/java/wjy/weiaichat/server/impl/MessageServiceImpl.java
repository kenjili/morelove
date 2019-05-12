package wjy.weiaichat.server.impl;

import io.netty.util.internal.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wjy.weiaichat.bean.Message;
import wjy.weiaichat.server.MessageService;
import wjy.weiaichat.dao.MessageDao;
import wjy.weiaichat.dao.UserDao;
import wjy.weiaichat.session.User;

import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageDao messageDao;
    @Autowired
    private UserDao userDao;

    @Transactional
    @Override
    public boolean savaMessage(Message message) {
        if (message == null) return false;
        int rows = messageDao.savaMessage(message);
        if (rows == 1)
            return true;
        return false;
    }

    @Transactional
    @Override
    public List<Message> readAllNotReadMsg(String username, String sendUsername) {
        if (StringUtil.isNullOrEmpty(username) || StringUtil.isNullOrEmpty(sendUsername)) return null;
        User user = userDao.getUserByUsername(username);
        if (user == null) return null;
        User sendUser = userDao.getUserByUsername(sendUsername);
        if (sendUser == null) return null;
        List<Message> messages = messageDao.allNotReadMsg(user.getId(), sendUser.getId());
        if(messages!=null&&messages.size()>0) {
            //将未读改为已读
            messageDao.updateMsgState(user.getId(), sendUser.getId(),messages.get(0).getCreateDate(),0,1);
        }
        return messages;
    }

    @Override
    public List<Message> getMsgWith(String username, String sendUsername, int page, int pageSize) {
        if (StringUtil.isNullOrEmpty(username) || StringUtil.isNullOrEmpty(sendUsername)) return null;
        User user = userDao.getUserByUsername(username);
        if (user == null) return null;
        User sendUser = userDao.getUserByUsername(sendUsername);
        if (sendUser == null) return null;
        page = page < 1 ? 1 : page;
        List<Message> messages = messageDao.getMsgHistory(user.getId(), sendUser.getId(),
                (page - 1) * pageSize, pageSize);
        return messages;
    }
}
