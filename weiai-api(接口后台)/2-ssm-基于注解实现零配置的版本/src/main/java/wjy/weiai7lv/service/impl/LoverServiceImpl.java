package wjy.weiai7lv.service.impl;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wjy.weiai7lv.dao.LoverDao;
import wjy.weiai7lv.dao.UserDao;
import wjy.weiai7lv.entity.Lover;
import wjy.weiai7lv.entity.User;
import wjy.weiai7lv.exception.WebException;
import wjy.weiai7lv.service.LoverService;

import java.util.HashMap;
import java.util.Map;


@Service
public class LoverServiceImpl implements LoverService {


    @Autowired
    private LoverDao loverDao;

    @Autowired
    private UserDao userDao;

    @Transactional
    public Lover bindLover(String otherUsername) throws WebException{
        //获取通过shiro认证的用户信息
        String loginUsername = (String) SecurityUtils.getSubject().getPrincipal();
        User myUser = userDao.getUserWithUsername(loginUsername);
        User otherUser = userDao.getUserWithUsername(otherUsername);
        //查看自己是否已经绑定了或者被申请绑定
        Lover lover = loverDao.findLover(myUser.getId());
        if(lover!=null){
            if(lover.getState()!=0){
                throw new WebException("您有情侣绑定关系尚未确认，不可提交绑定！");
            }
            throw new WebException("您已经绑定过了！");
        }
        if(otherUser==null||otherUser.isDisable()){
            throw new WebException("对方用户'"+otherUsername+"'不存在！");
        }
        //判断对方是否是异性
        if(myUser.getSex()==otherUser.getSex()){
            throw new WebException("本系统暂时不支持同性恋哎！");
        }
        //判断对方是否已经被绑定或者被申请绑定
        lover = loverDao.findLover(otherUser.getId());
        if(lover!=null){
            if(lover.getState()==0){
                throw new WebException("对方已经是别人的小可爱啦！");
            }else{
                throw new WebException("对方不知道被哪个家伙追求了！");
            }
        }
        //开始绑定
        lover = new Lover();
        lover.setMan(myUser.getSex()==0?myUser:otherUser);
        lover.setWomen(myUser.getSex()==1?myUser:otherUser);
        lover.setState(otherUser.getId());//对方的id
        lover.setDetails("这对情侣很低调啦，还没有介绍！");
        loverDao.registLover(lover);
        lover = loverDao.findLover(myUser.getId());
        return lover;
    }

    /**
     * 返回客户端的状态跟数据库的不一样：
     * 0为绑定成功，
     * 1为等待对方通过申请，
     * 2为被申请绑定
     * @return
     */
    public Map<String, Object> bindState() {
        Map<String, Object> data = new HashMap<>();
        //获取通过shiro认证的用户信息
        String loginUsername = (String) SecurityUtils.getSubject().getPrincipal();
        User user = userDao.getUserWithUsername(loginUsername);
        Lover lover = loverDao.findLover(user.getId());
        if(lover==null)
            return data;
        int state = lover.getState();
        if(state==user.getId())state = 2;
        else if(state==0)state=0;
        else state = 1;
        data.put("state",state);
        //返回另一半的信息
        data.put("otherUser",lover.getMan().getId()==user.getId()?lover.getWomen():lover.getMan());
        return data;
    }


    @Transactional
    public Lover enterLover() throws WebException {
        //获取通过shiro认证的用户信息
        String loginUsername = (String) SecurityUtils.getSubject().getPrincipal();
        User myUser = userDao.getUserWithUsername(loginUsername);
        //查看自己是否已经绑定了或者被申请绑定
        Lover lover = loverDao.findLover(myUser.getId());
        if(lover==null||lover.getState()!=myUser.getId()){
            throw new WebException("没有情侣关系绑定申请！");
        }
        lover.setState(0);
        loverDao.updateLover(lover);
        return lover;
    }

    public Lover findLover() throws WebException {
        //获取通过shiro认证的用户信息
        String loginUsername = (String) SecurityUtils.getSubject().getPrincipal();
        User myUser = userDao.getUserWithUsername(loginUsername);
        //查看自己是否已经绑定了或者被申请绑定
        Lover lover = loverDao.findLover(myUser.getId());
        if(lover==null||lover.getState()!=0){
            throw new WebException("未确认情侣关系！");
        }
        return lover;
    }


}
