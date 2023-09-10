package com.ketty.common_base;

import com.ketty.common_utils.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @Auther: Ketty Allen
 * @Date:2023/2/3 - 23:30
 * @Description:com.ketty.common_base
 * @version: 1.0
 */
@Slf4j
@Configuration
public class JedisConfiguration {

    @Value("${spring.redis.host}")
    private String host;
    @Value("${spring.redis.port}")
    private int port;
    @Value("${spring.redis.timeout}")
    private int timeout;
    @Value("${spring.redis.jedis.pool.max-active}")
    private int maxActive;
    @Value("${spring.redis.jedis.pool.max-wait}")
    private int maxWait;
    @Value("${spring.redis.jedis.pool.max-idle}")
    private int maxIdle;
    @Value("${spring.redis.jedis.pool.min-idle}")
    private int minIdle;

    /**
     * jedis pool config
     * 配置连接池线程数（减少线程的频繁创建带来的开销）、超时时间（提高服务可用性）等参数。
     * @return
     */
    @Bean
    public JedisPoolConfig jedisPoolConfig() {
        JedisPoolConfig config = new JedisPoolConfig();
        /*最大空闲数*/
        config.setMaxIdle(maxIdle);
        /*连接池的最大数据库连接数*/
        config.setMaxTotal(10000);
        /*最大建立连接等待时间*/
        config.setMaxWaitMillis(maxWait);
        /*是否在从池中取出连接前进行检验,如果检验失败,则从池中去除连接并尝试取出另一个*/
        config.setTestOnBorrow(true);
        /*在空闲时检查有效性, 默认false*/
        config.setTestWhileIdle(false);
        return config;
    }

    @Bean
    public JedisPool jedisPool() {
        JedisPool jedisPool;
        jedisPool = new JedisPool(jedisPoolConfig(), host, port, timeout);
        RedisUtils.setJedisPool(jedisPool);
        return jedisPool;
    }

}
