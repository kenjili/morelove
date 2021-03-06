package wujiuye.morelove.chat.srv.service;




import wujiuye.morelove.chat.packet.bean.Message;

import java.util.List;

public interface MessageService {


    boolean savaMessage(Message message);

    /**
     * 获取所有未读消息
     * @param username
     * @return
     */
    List<Message> readAllNotReadMsg(String username,String sendUsername);


    /**
     * 分页获取历史消息
     * @param username
     * @param page
     * @param pageSize
     * @return
     */
    List<Message> getMsgWith(String username,String sendUsername,int page,int pageSize);

}
