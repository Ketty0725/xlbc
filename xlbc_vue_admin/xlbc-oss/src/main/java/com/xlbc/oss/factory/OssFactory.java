package com.xlbc.oss.factory;

import com.xlbc.common.constant.CacheNames;
import com.xlbc.common.utils.JsonUtils;
import com.xlbc.common.utils.StringUtils;
import com.xlbc.common.utils.redis.CacheUtils;
import com.xlbc.common.utils.redis.RedisUtils;
import com.xlbc.oss.constant.OssConstant;
import com.xlbc.oss.core.OssClient;
import com.xlbc.oss.exception.OssException;
import com.xlbc.oss.properties.OssProperties;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 文件上传Factory
 *
 * @author Lion Li
 */
@Slf4j
public class OssFactory {

    private static final Map<String, OssClient> CLIENT_CACHE = new ConcurrentHashMap<>();

    /**
     * 获取默认实例
     */
    public static OssClient instance() {
        // 获取redis 默认类型
        String configKey = RedisUtils.getCacheObject(OssConstant.DEFAULT_CONFIG_KEY);
        if (StringUtils.isEmpty(configKey)) {
            throw new OssException("文件存储服务类型无法找到!");
        }
        return instance(configKey);
    }

    /**
     * 根据类型获取实例
     */
    public static OssClient instance(String configKey) {
        String json = CacheUtils.get(CacheNames.SYS_OSS_CONFIG, configKey);
        if (json == null) {
            throw new OssException("系统异常, '" + configKey + "'配置信息不存在!");
        }
        OssProperties properties = JsonUtils.parseObject(json, OssProperties.class);
        OssClient client = CLIENT_CACHE.get(configKey);
        if (client == null) {
            CLIENT_CACHE.put(configKey, new OssClient(configKey, properties));
            log.info("创建OSS实例 key => {}", configKey);
            return CLIENT_CACHE.get(configKey);
        }
        // 配置不相同则重新构建
        if (!client.checkPropertiesSame(properties)) {
            CLIENT_CACHE.put(configKey, new OssClient(configKey, properties));
            log.info("重载OSS实例 key => {}", configKey);
            return CLIENT_CACHE.get(configKey);
        }
        return client;
    }

}
