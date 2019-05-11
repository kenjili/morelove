package com.wujiuye.weiai7lv;

import com.wujiuye.weiai7lv.dao.UserDao;
import com.wujiuye.weiai7lv.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Weiai7lvApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class Weiai7lvApplicationTests {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private UserDao userDao;

    @Test
    public void testMybatisPlugin(){
        User user = userDao.getUserWithUsername("wujiuye");
        System.out.println(user.getUsername());

//        userDao.deleteWhiteByTime_v1(new Date(),"wujiuye");
//        userDao.deleteWhiteByTime_v2(new Date());
//        userDao.deleteWhiteByTime_v3(new java.sql.Date(System.currentTimeMillis()));
    }
}
