package wujiuye.morelove.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.*;
import redis.clients.jedis.JedisPoolConfig;

import java.util.HashSet;
import java.util.Set;

@Configuration
public class RedisConfig{

    @Value("${spring.redis.host}")
    private String host;
    @Value("${spring.redis.port}")
    private Integer port;
    @Value("${spring.redis.database}")
    private Integer dbIndex;
    @Value("${spring.redis.timeout}")
    private Long expiration;

    @Value("${spring.redis.jedis.pool.max-active}")
    private Integer maxTotal;
    @Value("${spring.redis.jedis.pool.max-idle}")
    private Integer maxIdle;
    @Value("${spring.redis.jedis.pool.min-idle}")
    private Integer minIdle;
    @Value("${spring.redis.jedis.pool.max-wait}")
    private Integer maxWaitMillis;


    /**
     * 配置JedisPoolConfig实例,JedisPoolConfig是连接池的配置
     *
     * @return
     */
    @Bean
    public JedisPoolConfig jedisPoolConfig() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(maxTotal);
        jedisPoolConfig.setMaxIdle(maxIdle);
        jedisPoolConfig.setMinIdle(minIdle);
        //是否在从池中取出连接之前进行检验
        //如果检验失败则从池中去除该连接并尝试取出另一个
        //testOnBorrow: true
        jedisPoolConfig.setTestOnBorrow(true);
        return jedisPoolConfig;
    }

    /**
     * 配置JedisConnectionFactory，连接池工厂
     *
     * @param jedisPoolConfig JedisPoolConfig实例，使用@Autowired声明自动注入
     * @return
     */
    @Bean
    public JedisConnectionFactory jedisConnectionFactory(@Autowired JedisPoolConfig jedisPoolConfig) {
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
     *
     * @param jedisConnectionFactory
     * @return
     */
    @Bean
    public RedisTemplate redisTemplate(@Autowired JedisConnectionFactory jedisConnectionFactory) {
//        //设置序列化
//        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
//        ObjectMapper om = new ObjectMapper();
//        //属性为NULL不序列化
//        om.setSerializationInclusion(JsonInclude.Include.NON_NULL);
//        //反序列化时找不到的字段不处理
//        om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//        //设置任何级别的字段都可以自动识别
//        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
//        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
//        jackson2JsonRedisSerializer.setObjectMapper(om);

        // 配置redisTemplate
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(jedisConnectionFactory);
        //序列化配置，约定所有的key只能是string类型
        RedisSerializer stringSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(stringSerializer);
        redisTemplate.setHashKeySerializer(stringSerializer);
//        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
//        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);

        JdkSerializationRedisSerializer vSerializer = new JdkSerializationRedisSerializer(this.getClass().getClassLoader());
        redisTemplate.setValueSerializer(vSerializer);
        redisTemplate.setHashValueSerializer(vSerializer);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }


    /**
     * 配置一个缓存管理者，使用spring-data-redis提供的RedisCacheManager类配置，
     * RedisCacheManager实现了spring cache提供的CacheManager接口
     *
     * @param jedisConnectionFactory
     * @return
     */
    @Bean
    public RedisCacheManager redisCacheManager(@Autowired JedisConnectionFactory jedisConnectionFactory) {
        //注入缓存名称
        Set<String>  cacheNames = new HashSet<>();
        cacheNames.add("movieBoxOffice");

        // 初始化缓存管理器，在这里我们可以缓存的整体过期时间什么的，我这里默认没有配置 lg.info("初始化 -> [{}]", "CacheManager RedisCacheManager Start");
        RedisCacheManager.RedisCacheManagerBuilder builder = RedisCacheManager
                .RedisCacheManagerBuilder
                .fromConnectionFactory(jedisConnectionFactory)
                .initialCacheNames(cacheNames);

        return builder.build();

    }
}
