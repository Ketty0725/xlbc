package com.ketty.common_utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 基于RedisTemplate的Redis工具类
 */
@Component
@Slf4j
public class RedisTemplateUtils {
    @Resource
    private RedisTemplate redisTemplate;

    /**
     * 插入(覆盖)某个缓存
     * @param key
     * @param value
     * @param timeToLive 过期时间，单位秒
     * */
    public boolean put(String key, Object value, int timeToLive) {
        if (key == null || value == null) {
            return false;
        }
        if(redisTemplate==null){
            log.debug("redisTemplate is null");
            return false;
        }
        try {
            if (timeToLive > 0) {
                redisTemplate.opsForValue().set(key, value, timeToLive, TimeUnit.SECONDS);
            }else{
                redisTemplate.opsForValue().set(key,value);
            }
            return true;
        } catch (Exception e) {
            log.error("redis util string key put exception", e);
        }
        return false;
    }

    /**
     * 插入(覆盖)某个缓存
     * @param key
     * @param value
     * @param timeToLive 过期时间，单位秒
     * */
    public boolean put(byte[] key, Object value, int timeToLive) {
        if (key == null || value == null){
            return false;
        }
        if(redisTemplate==null){
            log.debug("redisTemplate is null");
            return false;
        }
        try {
            if (timeToLive > 0) {
                redisTemplate.opsForValue().set(key, value, timeToLive, TimeUnit.SECONDS);
            }else{
                redisTemplate.opsForValue().set(key,value);
            }
            return true;
        } catch (Exception e) {
            log.error("redis util byte key put exception", e);
        }
        return false;
    }

    /**
     * 根据key获取缓存
     * @param key
     * */
    public Object get(String key) {
        if (key == null) {
            return null;
        }
        if(redisTemplate==null){
            log.debug("redisTemplate is null");
            return null;
        }
        try {
            return redisTemplate.opsForValue().get(key);
        } catch (Exception e) {
            log.error("redis util get exception", e);
            return null;
        }
    }

    /**
     * 根据key获取缓存
     * @param key
     * */
    public byte[] getByte(byte[] key) {
        if (key == null) {
            return null;
        }
        if(redisTemplate==null){
            log.debug("redisTemplate is null");
            return null;
        }
        try {
            return SerializeUtil.serialize(redisTemplate.opsForValue().get(key));
        } catch (Exception e) {
            log.error("redis util getByte exception", e);
            return null;
        }
    }

    /**
     * 根据key获取缓存
     * @param key
     * */
    public Object get(byte[] key) {
        if (key == null) {
            return null;
        }
        if(redisTemplate==null){
            log.debug("redisTemplate is null");
            return null;
        }
        try {
            return redisTemplate.opsForValue().get(key);
        } catch (Exception e) {
            log.error("redis util getObject exception", e);
            return null;
        }
    }

    /**
     * 递增某个数据（这个数据没有被序列化，不能用get查询）
     * @param key
     * @param delta
     * @param timeToLive 超时时间，单位秒
     * @return
     */
    public long incr(String key, long delta, int timeToLive) {
        if (key == null){
            return -1;
        }
        if(redisTemplate==null){
            log.debug("redisTemplate is null");
            return -1;
        }
        try {
            long result = redisTemplate.opsForValue().increment(key,delta);
            if (timeToLive > 0) {
                redisTemplate.expire(key, timeToLive, TimeUnit.SECONDS);
            }
            return result;
        } catch (Exception e) {
            log.error("redis util incr exception", e);
            return -1;
        }
    }
    /**
     * 删除某个缓存
     * @param key
     * */
    public boolean delete(String key) {
        if (key == null) {
            return false;
        }
        if(redisTemplate==null){
            log.debug("redisTemplate is null");
            return false;
        }
        try {
            redisTemplate.delete(key);
            return true;
        } catch (Exception e) {
            log.error("redis util delete exception", e);
            return false;
        }
    }

    /**
     * 插原子缓存,同名key已存在返回false
     * @param key
     * @param value
     * @param timeToLive 超时时间，单位秒
     * */
    public boolean setAtomCache(String key, Object value, int timeToLive) {
        if (key == null || value == null) {
            return false;
        }
        if(redisTemplate==null){
            log.debug("redisTemplate is null");
            return false;
        }

        boolean success = false;
        try {
            // 不存在则添加缓存
            success = redisTemplate.opsForValue().setIfAbsent(key, value);
        } catch (Exception e) {
            log.error("redis util setAtomCache-setIfAbsent exception", e);
        }
        // 如果存活时间大于0，则设置存活时间
        if (timeToLive > 0){
            try {
                redisTemplate.expire(key, timeToLive,TimeUnit.SECONDS);
            } catch (Exception e) {
                log.error("redis util setAtomCache-expire exception", e);
            }
        }
        return success;
    }

    /**
     * 判断key是否存在
     * @param key
     * @return
     */
    public boolean exists(Object key){
        return redisTemplate.hasKey(key);
    }

    /**
     * 根据关键值模糊匹配
     * @param keyPer
     * @return
     */
    public Set<String> keys(String keyPer){
        return redisTemplate.keys(keyPer);
    }
}
