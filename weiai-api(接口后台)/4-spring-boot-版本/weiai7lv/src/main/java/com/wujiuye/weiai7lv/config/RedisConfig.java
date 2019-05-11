package com.wujiuye.weiai7lv.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

//@Configuration
public class RedisConfig{

    @Value("${redis.host}")
    private String host;
    @Value("${redis.port}")
    private Integer port;
    @Value("${redis.dbIndex}")
    private Integer dbIndex;

    @Value("${redis.maxTotal}")
    private Integer maxTotal;
    @Value("${redis.maxIdle}")
    private Integer maxIdle;
    @Value("${redis.minIdle}")
    private Integer minIdle;
    @Value("${redis.maxWaitMillis}")
    private Integer maxWaitMillis;
    @Value("${redis.testOnBorrow}")
    private Boolean testOnBorrow;
    @Value("${redis.expiration}")
    private Long expiration;

    /**
     * 配置JedisPoolConfig实例,JedisPoolConfig是连接池的配置
     * @return
     */
    @Bean
    public JedisPoolConfig jedisPoolConfig(){
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(maxTotal);
        jedisPoolConfig.setMaxIdle(maxIdle);
        jedisPoolConfig.setMinIdle(minIdle);
        jedisPoolConfig.setTestOnBorrow(testOnBorrow);
        return jedisPoolConfig;
    }

    /**
     * 配置JedisConnectionFactory，连接池工厂
     * @param jedisPoolConfig JedisPoolConfig实例，使用@Autowired声明自动注入
     * @return
     */
    @Bean
    public JedisConnectionFactory jedisConnectionFactory(@Autowired JedisPoolConfig jedisPoolConfig){
        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory();
        jedisConnectionFactory.setUsePool(true);
        jedisConnectionFactory.setPoolConfig(jedisPoolConfig);
        jedisConnectionFactory.setDatabase(dbIndex);
        jedisConnectionFactory.setHostName(host);
        jedisConnectionFactory.setPort(port);
        return jedisConnectionFactory;
    }


    /**
     * 配置操作redis的RedisTemplate对象
     * spring-data-redis封装了RedisTemplate对象来进行对Redis的各种操作，它支持所有的Redis原生的api。
     * @param jedisConnectionFactory
     * @return
     */
    @Bean
    public RedisTemplate redisTemplate(@Autowired JedisConnectionFactory jedisConnectionFactory){
        RedisTemplate redisTemplate = new RedisTemplate();
        redisTemplate.setEnableTransactionSupport(true);
        redisTemplate.setConnectionFactory(jedisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new JdkSerializationRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new JdkSerializationRedisSerializer());
        return redisTemplate;
    }

    /**
     * 配置一个缓存管理者，使用spring-data-redis提供的RedisCacheManager类配置，
     *         RedisCacheManager实现了spring cache提供的CacheManager接口
     * @param redisTemplate
     * @return
     */
    @Bean
    public RedisCacheManager redisCacheManager(@Autowired RedisTemplate redisTemplate){
        RedisCacheManager redisCacheManager = new RedisCacheManager(redisTemplate);

        //注入缓存名称
        Set<String>  cacheNames = new HashSet<>();
        cacheNames.add("userCache");
        cacheNames.add("rolesCache");
        cacheNames.add("movieBoxOffice");
        redisCacheManager.setCacheNames(cacheNames);

        //默认缓存过期时间
        redisCacheManager.setDefaultExpiration(expiration);

        //指定某个缓存名称下的缓存的过期时间
        Map<String,Long> expirationMap = new HashMap<>();
        expirationMap.put("userCache",3600l);
        redisCacheManager.setExpires(expirationMap);

        return redisCacheManager;
    }
}
