package wujiuye.morelove.chat.srv.service.impl;

import io.netty.util.internal.StringUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wujiuye.morelove.chat.packet.bean.Message;
import wujiuye.morelove.chat.packet.bean.User;
import wujiuye.morelove.chat.srv.dao.MessageDao;
import wujiuye.morelove.chat.srv.dao.UserDao;
import wujiuye.morelove.chat.srv.service.MessageService;

import javax.annotation.Resource;
import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {

    @Resource
    private MessageDao messageDao;
    @Resource
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
