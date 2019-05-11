package com.wujiuye.config;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.CacheErrorHandler;
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
@EnableCaching
// 配合TransactionManagementConfigurer可指定事务管理者，
// 为@TransactionManager注解配置默认的缓存管理者
@EnableTransactionManagement
@Slf4j
public class TxAndCacheConfig extends CachingConfigurerSupport implements TransactionManagementConfigurer {

    /**
     * 在MyBatisConfig中配置了dataSourceTransactionManager，所以@Autowired注入的就是dataSourceTransactionManager
     */
    @Autowired
    private PlatformTransactionManager dataSourceTransactionManager;

    /**
     * 在RedisConfig中配置了redisCacheManager，所以@Autowired注入的就是redisCacheManager
     */
    @Autowired
    private CacheManager cacheManager;

    @Override
    public PlatformTransactionManager annotationDrivenTransactionManager() {
        return dataSourceTransactionManager;
    }


    @Override
    public CacheManager cacheManager() {
        return cacheManager;
    }

    /**
     * 缓存异常处理
     * @return
     */
    @Override
    public CacheErrorHandler errorHandler() {
        CacheErrorHandler cacheErrorHandler = new CacheErrorHandler() {

            /**
             * 获取缓存出错
             * @param e
             * @param cache
             * @param key
             */
            @Override
            public void handleCacheGetError(RuntimeException e, Cache cache, Object key) {
                log.error("Redis occur handleCacheGetError：key -> [{}]", key, e);
            }

            /**
             * 写入缓存出错
             * @param e
             * @param cache
             * @param key
             * @param value
             */
            @Override
            public void handleCachePutError(RuntimeException e, Cache cache, Object key, Object value) {
                log.error("Redis occur handleCachePutError：key -> [{}]；value -> [{}]", key, value, e);
            }

            /**
             * 删除缓存出错
             * @param e
             * @param cache
             * @param key
             */
            @Override
            public void handleCacheEvictError(RuntimeException e, Cache cache, Object key) {
                log.error("Redis occur handleCacheEvictError：key -> [{}]", key, e);
            }

            /**
             * 清空缓存出错
             * @param e
             * @param cache
             */
            @Override
            public void handleCacheClearError(RuntimeException e, Cache cache) {
                log.error("Redis occur handleCacheClearError：", e);
            }
        };
        return cacheErrorHandler;

    }
}
