package wujiuye.morelove.business.service.impl;

import wujiuye.morelove.business.service.MemorialdayService;
import wujiuye.morelove.common.config.WebConstantConfig;
import wujiuye.morelove.common.exception.WebException;
import wujiuye.morelove.common.utils.StringUtils;
import wujiuye.morelove.common.utils.UploadImageFileUtils;
import wujiuye.morelove.dao.LoverDao;
import wujiuye.morelove.dao.UserDao;
import wujiuye.morelove.dao.MemorialdayDao;
import wujiuye.morelove.pojo.Lover;
import wujiuye.morelove.pojo.User;
import wujiuye.morelove.pojo.MemorialDay;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@Transactional
public class MemorialdayServiceImpl implements MemorialdayService {

    @Autowired
    private WebConstantConfig webConstantConfig;

    @Autowired
    private MemorialdayDao memorialdayDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private LoverDao loverDao;

    @Override
    public void savaMemorialday(MemorialDay memorialDay, MultipartFile img) {
        if (StringUtils.strIsNull(memorialDay.getMemorialName())) {
            throw new WebException("名称不能为空!");
        } else if (memorialDay.getMemorialDate() == null) {
            throw new WebException("请选择日期!");
        }
        //获取通过shiro认证的用户信息
        String username = (String) SecurityUtils.getSubject().getPrincipal();
        User user = userDao.getUserWithUsername(username);
        Lover lover = loverDao.findLover(user.getId());
        memorialDay.setLover(lover);
        String savaPath = "/lover-" + lover.getId();
        String[] paths = UploadImageFileUtils.savaImageFile(img, webConstantConfig.getPrivateUploadFileRootPath(), webConstantConfig.getMemorialDayImagesRootPath() + savaPath);
        memorialDay.setImage(paths[0]);
        memorialdayDao.savaMemorialDay(memorialDay);
    }


    @Override
    public void delteMemorialday(int id) {
        //获取通过shiro认证的用户信息
        String username = (String) SecurityUtils.getSubject().getPrincipal();
        User user = userDao.getUserWithUsername(username);
        //验证loveId是否是自己的
        Lover lover = loverDao.findLover(user.getId());
        if (lover != null) {
            memorialdayDao.delMemorialDay(id, lover.getId());
        }
    }

    @Override
    public List<MemorialDay> allMemorialday() {
        //获取通过shiro认证的用户信息
        String username = (String) SecurityUtils.getSubject().getPrincipal();
        User user = userDao.getUserWithUsername(username);
        Lover lover = loverDao.findLover(user.getId());
        List<MemorialDay> memorialDayList = memorialdayDao.getAllMemorialDay(lover.getId());
        if (memorialDayList != null && memorialDayList.size() > 0) {
            for (MemorialDay memorialDay : memorialDayList) {
                memorialDay.setImage(webConstantConfig.getPrivateResourceServerName() + memorialDay.getImage());
            }
        }
        return memorialDayList;
    }

    @Override
    public int getCount() {
        //获取通过shiro认证的用户信息
        String username = (String) SecurityUtils.getSubject().getPrincipal();
        User user = userDao.getUserWithUsername(username);
        Lover lover = loverDao.findLover(user.getId());
        if (lover != null)
            return memorialdayDao.getCount(lover.getId());
        return 0;
    }
}
