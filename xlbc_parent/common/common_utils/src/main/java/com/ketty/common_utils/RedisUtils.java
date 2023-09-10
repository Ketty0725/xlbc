package com.ketty.common_utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisConnectionException;
import redis.clients.jedis.params.SetParams;

/**
 * @Auther: Ketty Allen
 * @Date:2023/2/3 - 23:42
 * @Description:com.ketty.common_utils
 * @version: 1.0
 */
public class RedisUtils {
    private static Logger logger = LoggerFactory.getLogger(RedisUtils.class);

    /**
     * SET IF NOT EXIST，即当key不存在时，我们进行set操作；若key已经存在，则不做任何操作；
     */
    private static final String SET_IF_NOT_EXIST = "NX";
    /**
     * 要给这个key加一个过期的设置，具体时间由第五个参数决定
     */
    private static final String SET_WITH_EXPIRE_TIME = "PX";

    private static final String LOCK_SUCCESS = "OK";


    private static JedisPool jedisPool;

    public static void setJedisPool(JedisPool jedisPool) {
        RedisUtils.jedisPool = jedisPool;
    }

    /**
     * jedis set方法，通过设置值过期时间exTime,单位:秒<br>
     * 为后期session服务器共享，Redis存储用户session所准备
     *
     * @param key    key
     * @param value  value
     * @param exTime 过期时间,单位:秒
     * @return 执行成功则返回result 否则返回null
     */
    public static String setEx(String key, String value, int exTime) {
        Jedis jedis = null;
        String result = null;
        try {
            jedis = jedisPool.getResource();
            result = jedis.setex(key, exTime, value);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return result;
        } finally {
            close(jedis);
        }
        return result;
    }

    public static boolean redisLock(String key, String value, int seconds) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            SetParams setParams = new SetParams();
            setParams.nx();
            setParams.px(seconds);
            String result = jedis.set(key, value, setParams);
            if (StringUtils.isNotBlank(result) && LOCK_SUCCESS.equals(result)) {
                return true;
            }
        } catch (JedisConnectionException ex) {
            logger.error(String.format("尝试加锁失败:%s", key));
            return false;
        } finally {
            close(jedis);
        }
        return false;
    }


    /**
     * 对key所对应的值进行重置过期时间expire
     *
     * @param key key
     * @param exTime 过期时间 单位:秒
     * @return 返回重置结果, 1:时间已经被重置，0:时间未被重置
     */
    public static Long expire(String key, int exTime) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.expire(key, exTime);
        } catch (JedisConnectionException ex) {
            logger.error(String.format("expireTtl error %s", key), ex);
        } finally {
            close(jedis);
        }
        return 0L;
    }

    /**
     * jedis set方法
     *
     * @param key   key
     * @param value value
     * @return 执行成功则返回result，否则返回null
     */
    public static String set(String key, String value) {
        Jedis jedis = null;
        String result = null;
        try {
            jedis = jedisPool.getResource();
            result = jedis.set(key, value);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return result;
        } finally {
            close(jedis);
        }
        return result;
    }

    /**
     * jedis get方法
     *
     * @param key key
     * @return 返回key对应的value 异常则返回null
     */
    public static String get(String key) {
        Jedis jedis = null;
        String result = null;
        try {
            jedis = jedisPool.getResource();
            result = jedis.get(key);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return result;
        } finally {
            close(jedis);
        }
        return result;
    }

    /**
     * jedis 删除方法
     *
     * @param key key
     * @return 返回结果，异常返回null
     */
    public static Long del(String key) {
        Jedis jedis = null;
        Long result = null;
        try {
            jedis = jedisPool.getResource();
            result = jedis.del(key);
        } catch (JedisConnectionException ex) {
            logger.error(String.format("Release remove error %s", key), ex);
            return result;
        } finally {
            close(jedis);
        }
        return result;
    }

    public static Long incrBy(String key, String val) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.incrBy(key, NumberUtils.toLong(val));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            close(jedis);
        }
        return null;
    }

    private static void close(Jedis jedis) {
        if (jedis != null) {
            jedis.close();
        }
    }
}
