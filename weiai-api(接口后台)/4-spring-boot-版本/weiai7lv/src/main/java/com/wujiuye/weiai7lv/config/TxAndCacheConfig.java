package com.wujiuye.weiai7lv.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;

/**
 * 开启缓存
 * 开启事务管理
 */
@Configuration
// 配合CachingConfigurerSupport可指定缓存管理者，
// 否则spring从bean容器中查找一个实现CacheManager的对象
//@EnableCaching
// 配合TransactionManagementConfigurer可指定事务管理者，
// 为@TransactionManager注解配置默认的缓存管理者
@EnableTransactionManagement
public class TxAndCacheConfig /*extends CachingConfigurerSupport*/ implements TransactionManagementConfigurer {

    /**
     * 在MyBatisConfig中配置了dataSourceTransactionManager，所以@Autowired注入的就是dataSourceTransactionManager
     */
    @Autowired
    private PlatformTransactionManager dataSourceTransactionManager;

    /**
     * 在RedisConfig中配置了redisCacheManager，所以@Autowired注入的就是redisCacheManager
     */
    //@Autowired
    //private CacheManager cacheManager;

    @Override
    public PlatformTransactionManager annotationDrivenTransactionManager() {
        return dataSourceTransactionManager;
    }


//    @Override
//    public CacheManager cacheManager() {
//        return cacheManager;
//    }
}
