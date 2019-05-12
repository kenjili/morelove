package wjy.weiaichat.dao;

import org.apache.ibatis.annotations.Param;
import wjy.weiaichat.bean.Message;

import java.util.Date;
import java.util.List;

public interface MessageDao {

    /**
     * 保存一条消息记录
     *
     * @param message
     * @return
     */
    int savaMessage(Message message);

    /**
     * 获取所有未读消息
     *
     * @param readUser 消息接收方的用户名
     * @param sendUser 发送方
     * @return
     */
    List<Message> allNotReadMsg(@Param("readUserId") Integer readUser, @Param("sendUserId") Integer sendUser);

    /**
     * 更新消息状态
     *
     * @param beforeDate 指定时间之前的
     * @param dscState   源状态
     * @param tagerState 目标状态
     * @return
     */
    int updateMsgState(@Param("readUserId") Integer readUser, @Param("sendUserId") Integer sendUser,
                       @Param("beforeDate") Date beforeDate, @Param("dscState") int dscState, @Param("tagerState") int tagerState);

    List<Message> getMsgHistory(@Param("readUserId") Integer readUser, @Param("sendUserId") Integer sendUser,
                                @Param("startIndex") Integer startIndex, @Param("pageSize") int pageSize);
}
