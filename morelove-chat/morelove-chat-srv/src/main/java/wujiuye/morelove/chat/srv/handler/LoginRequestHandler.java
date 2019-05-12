package wujiuye.morelove.chat.srv.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.stereotype.Component;
import wujiuye.morelove.chat.packet.bean.User;
import wujiuye.morelove.chat.packet.request.LoginRequestPacket;
import wujiuye.morelove.chat.packet.response.LoginResponsePacket;
import wujiuye.morelove.chat.srv.service.UserService;
import wujiuye.morelove.chat.srv.session.UserManager;


import javax.annotation.Resource;
import java.util.Date;

@Component
public class LoginRequestHandler implements FactoryBean<LoginRequestHandler.LoginChannelRequestHandler> {


    @Resource(name = "userServiceImpl")
    private UserService userService;

    public class LoginChannelRequestHandler extends SimpleChannelInboundHandler<LoginRequestPacket> {
        @Override
        protected void channelRead0(ChannelHandlerContext ctx, LoginRequestPacket loginRequestPacket) {
            LoginResponsePacket loginResponsePacket = new LoginResponsePacket();
            loginResponsePacket.setVersion(loginRequestPacket.getVersion());
            loginResponsePacket.setUserName(loginRequestPacket.getUserName());

            if (valid(loginRequestPacket)) {
                loginResponsePacket.setSuccess(true);
                User user = new User();
                user.setUsername(loginRequestPacket.getUserName());
                user.setPassword(loginRequestPacket.getPassword());
                System.out.println("[" + loginRequestPacket.getUserName() + "]登录成功");
                UserManager.bindUser(user, ctx.channel());
            } else {
                loginResponsePacket.setReason("账号密码校验失败");
                loginResponsePacket.setSuccess(false);
                System.out.println(new Date() + ": 登录失败!");
            }

            // 登录响应
            ctx.channel().writeAndFlush(loginResponsePacket);
        }

        /**
         * 验证用户信息
         *
         * @param loginRequestPacket
         * @return
         */
        private boolean valid(LoginRequestPacket loginRequestPacket) {
            User user = new User();
            user.setUsername(loginRequestPacket.getUserName());
            user.setPassword(loginRequestPacket.getPassword());
            return userService.validationRegist(user);
        }


        /**
         * channel不活跃（连接关闭）
         *
         * @param ctx
         */
        @Override
        public void channelInactive(ChannelHandlerContext ctx) {
            System.out.println("已断开连接");
            UserManager.unBindUser(ctx.channel());
        }
    }



    @Override
    public LoginChannelRequestHandler getObject() throws Exception {
        return new LoginChannelRequestHandler();
    }

    @Override
    public Class<?> getObjectType() {
        return LoginChannelRequestHandler.class;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }
}
