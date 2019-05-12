package wujiuye.morelove.chat.srv.session;

import io.netty.channel.Channel;
import io.netty.util.AttributeKey;
import wujiuye.morelove.chat.packet.bean.User;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UserManager {

    /**
     * 用户和Channel全局映射
     */
    private static final Map<String, Channel> userChannelMap = new ConcurrentHashMap<>();
    private static AttributeKey<User> LOGIN_USER = AttributeKey.newInstance("user");

    /**
     * 绑定
     * @param user
     * @param channel
     */
    public static void bindUser(User user, Channel channel) {
        userChannelMap.put(user.getUsername(), channel);
        channel.attr(LOGIN_USER).set(user);
    }

    /**
     * 解绑session
     * @param channel
     */
    public static void unBindUser(Channel channel) {
        if (hasLogin(channel)) {
            userChannelMap.remove(getUser(channel).getUsername());
            channel.attr(LOGIN_USER).set(null);
        }
    }

    /**
     * 判断用户是否登录
     * @param channel
     * @return
     */
    public static boolean hasLogin(Channel channel) {
        return channel.hasAttr(LOGIN_USER);
    }

    /**
     * 获取用户的session
     * @param channel
     * @return
     */
    public static User getUser(Channel channel) {
        return channel.attr(LOGIN_USER).get();
    }

    /**
     * 登录用户名获取Channel
     * @param username
     * @return
     */
    public static Channel getChannel(String username) {
        return userChannelMap.get(username);
    }
}
