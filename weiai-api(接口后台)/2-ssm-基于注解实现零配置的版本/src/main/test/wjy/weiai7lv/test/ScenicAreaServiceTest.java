package wjy.weiai7lv.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import wjy.weiai7lv.dao.ScenicAreaDao;
import wjy.weiai7lv.entity.ScenicArea;
import wjy.weiai7lv.service.ScenicAreaService;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/spring.xml")
public class ScenicAreaServiceTest {

    @Autowired
    private ScenicAreaService scenicAreaService;

    @Test
    public void test(){

        List<ScenicArea> list = scenicAreaService.searchScenicArea("北京",1,10);
        System.out.println(list.size());

    }

}
