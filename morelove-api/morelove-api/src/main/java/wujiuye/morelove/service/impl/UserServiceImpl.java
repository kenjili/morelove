package wujiuye.morelove.service.impl;

import com.sun.istack.NotNull;
import wujiuye.morelove.common.config.WebConstantConfig;
import wujiuye.morelove.common.exception.WebException;
import wujiuye.morelove.common.utils.StringUtils;
import wujiuye.morelove.dao.UserDao;
import wujiuye.morelove.pojo.User;
import wujiuye.morelove.service.UserService;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * 用户相关业务类
 *
 * @author wjy
 * @Cacheable注解标注该方法查询的结果存入缓存，再次访问时直接读取缓存中的数据。常用配置属性解析： value：该缓存名称下所缓存的所有的key是一个zset类型集合。
 * （可以使用redis-cli的type key命令查看指定的key是什么类型的数据）
 * key：就是缓存该方法返回的数据的key
 * 配置value等同于配置cacheNames，配置cacheNames也等同于配置value
 */
//@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")//让idea对@Autowired等自动注入不警告
@Service
// 使用的是jdbc的事务管理，所以@Transactional只是提供给mybatis用的，redis不是
// @Transactional(transactionManager = "dataSourceTransactionManager")指定事务管理者，不指定则使用默认的
@Transactional
public class UserServiceImpl implements UserService {


    @Autowired
    private UserDao userDao;

    /**
     * 使用@CacheEvict清掉缓存对应记录
     *
     * @param user
     * @return
     */
    @Override
    public void userLogout(User user) {

    }


    /**
     * 查询用户信息
     *
     * @param username 用户名
     * @return
     */
    public User getUserWithUsername(String username) {
        if (StringUtils.strIsNull(username))
            return null;
        return this.userDao.getUserWithUsername(username);
    }

    /**
     * 注意，不要与缓存用户使用同一个key,否则会导致你意向不到的错误
     * 比如：从redis获取了key相同的数据类型是User,结果强制类型转换为List<String>就会报异常了。
     *
     * @param username
     * @return
     */
    public List<String> rolesListWithUser(String username) {
        if (StringUtils.strIsNull(username))
            return new ArrayList<>();
        return userDao.rolesListWithUser(username);
    }

    /**
     * 注册用户,异常回滚
     *
     * @return
     */
    public User registUser(@NotNull User user) throws WebException {
        //判断用户名(长度、字符)
        if (!StringUtils.isUsername(user.getUsername()))
            throw new WebException("用户名必须是2～8位中文字符！");
        //判断密码（长度、字符）
        if (!StringUtils.isPassword(user.getPassword()))
            throw new WebException("密码必须是英文和字母组合，且密码长度必须为6～18位！");
        //判断真实姓名
        if (!StringUtils.isUsername(user.getRealname()))
            throw new WebException("真实姓名必须是2～8位中文字符！");
        //判断手机号是否正确
        if (!StringUtils.isPhoneNumber(user.getPhoneNumber()))
            throw new WebException("请输入11位有效的手机号码！");
        //判断手机号码是否已经被注册
        User exUser = userDao.getUserWithPhone(user.getPhoneNumber());
        if (exUser != null)
            throw new WebException("手机号'" + user.getPhoneNumber() + "'已经被注册！");
        //判断用户是否已经存在
        exUser = userDao.getUserWithUsername(user.getUsername());
        if (exUser != null)
            throw new WebException("用户名'" + user.getUsername() + "'已经存在！");
        //加密密码
        String newPassword = new SimpleHash(WebConstantConfig.ALGORITH_NAME, user.getPassword(), ByteSource.Util.bytes(WebConstantConfig.SALT), WebConstantConfig.HASH_ITERATIONS).toHex();
        user.setPassword(newPassword);
        //持久化数据
        userDao.registUser(user);
        return user;
    }

    public Map<String, Object> queryUserLoginInfo(User user) {
        if (user == null || user.getId() == null)
            return null;
        return userDao.queryUserLoginInfo(user);
    }

    public int addUserLoginInfo(User user) {
        if (user == null || user.getId() == null)
            return 0;
        return userDao.addUserLoginInfo(user.getId());
    }


    /**
     * 更新用户登录状态信息
     *
     * @param user
     * @param lastLoginDevice    登录设备
     * @param passwordErrorCount 密码错误次数
     * @return
     */
    public int updateUserLoginInfo(User user, String lastLoginDevice, Integer passwordErrorCount) {
        if (user == null || user.getId() == null)
            return 0;
        //密码错误4次以上禁用该用户的登录功能,触发器更改了，所以不用在这里设置了
        //禁用登录功能之后在指定的时候后可登录，已在触发器中完成
        return userDao.updateUserLoginInfo(user.getId(),
                lastLoginDevice,
                new Date(),
                passwordErrorCount);
    }
}
