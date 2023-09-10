package com.ketty.serviceimpl;

import com.ketty.api_entity.*;
import com.ketty.api_entity.enums.CollectStateEnum;
import com.ketty.api_entity.enums.LikedStateEnum;
import com.ketty.common_utils.RedisKeyUtils;
import com.ketty.service.RedisService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Service;

import java.text.Collator;
import java.text.SimpleDateFormat;
import java.util.*;

import static net.sf.jsqlparser.util.validation.metadata.NamedObject.user;

/**
 * @Auther: Ketty Allen
 * @Date:2023/2/2 - 23:46
 * @Description:com.ketty.api_service.impl
 * @version: 1.0
 */
@Service
@Slf4j
public class RedisServiceImpl implements RedisService {
    @Autowired
    RedisTemplate redisTemplate;

    private void saveToRedis(Object mapKey, Object key1, Object key2, Object value) {
        String key = RedisKeyUtils.getHashKey(key1, key2);
        redisTemplate.opsForHash().put(mapKey, key, value);
    }

    private void saveToRedis(Object mapKey, Object key1, Object key2, Object key3, Object value) {
        String key = RedisKeyUtils.getHashKey(key1, key2, key3);
        redisTemplate.opsForHash().put(mapKey, key, value);
    }

    private void deleteFromRedis(Object mapKey, Object key1, Object key2) {
        String key = RedisKeyUtils.getHashKey(key1, key2);
        redisTemplate.opsForHash().delete(mapKey, key);
    }

    private void deleteFromRedis(Object mapKey, Object key1, Object key2, Object key3) {
        String key = RedisKeyUtils.getHashKey(key1, key2, key3);
        redisTemplate.opsForHash().delete(mapKey, key);
    }

    private void deleteBatchFromRedis(Object mapKey, Object key) {
        Cursor<Map.Entry<Object, Object>> cursor =
                redisTemplate.opsForHash().scan(mapKey,
                        ScanOptions.scanOptions().match("*" + key + "*").build());
        while (cursor.hasNext()) {
            Map.Entry<Object, Object> entry = cursor.next();
            Object hashKey = entry.getKey();
            redisTemplate.opsForHash().delete(mapKey, hashKey);
        }
        cursor.close();
    }

    private void deleteBatchFromRedis(Object mapKey, Object hashKey1, Object hashKey2) {
        Cursor<Map.Entry<Object, Object>> cursor =
                redisTemplate.opsForHash().scan(mapKey,
                        ScanOptions.scanOptions()
                                .match("*" + hashKey1 + "*" + hashKey2 + "*").build());
        while (cursor.hasNext()) {
            Map.Entry<Object, Object> entry = cursor.next();
            Object hashKey = entry.getKey();
            redisTemplate.opsForHash().delete(mapKey, hashKey);
        }
        cursor.close();
    }

    private void incrementCount(Object mapKey, Object key1, Object key2, long value) {
        String key = RedisKeyUtils.getHashKey(key1, key2);
        redisTemplate.opsForHash().increment(mapKey, key, value);
    }

    private void incrementCount(Object mapKey, Object key1, Object key2, Object key3, long value) {
        String key = RedisKeyUtils.getHashKey(key1, key2, key3);
        redisTemplate.opsForHash().increment(mapKey, key, value);
    }

    private Object getValueFromRedis(Object mapKey, Object key1, Object key2) {
        String key = RedisKeyUtils.getHashKey(key1, key2);
        Object value = redisTemplate.opsForHash().get(mapKey, key);
        return value;
    }

    private Object getValueFromRedis(Object mapKey, Object key1, Object key2, Object key3) {
        String key = RedisKeyUtils.getHashKey(key1, key2, key3);
        Object value = redisTemplate.opsForHash().get(mapKey, key);
        return value;
    }

    private List<String[]> getDataFromRedis(Object mapKey) {
        Cursor<Map.Entry<Object, Object>> cursor = redisTemplate.opsForHash().scan(mapKey, ScanOptions.NONE);
        List<String[]> list = new ArrayList<>();
        while (cursor.hasNext()) {
            Map.Entry<Object, Object> entry = cursor.next();
            String key = (String) entry.getKey();
            String[] split = key.split("::");
            String[] datas = new String[split.length + 1];
            for (int i = 0; i < split.length; i++) {
                datas[i] = split[i];
            }
            String value = String.valueOf(entry.getValue());
            datas[split.length] = value;
            list.add(datas);

            redisTemplate.opsForHash().delete(mapKey, key);
        }
        cursor.close();
        return list;
    }

    private List<String[]> obscureSearch(Object mapKey, Object hashKey) {
        Cursor<Map.Entry<Object, Object>> cursor =
                redisTemplate.opsForHash().scan(mapKey,
                        ScanOptions.scanOptions().match("?" + hashKey + "*").build());
        List<String[]> list = new ArrayList<>();
        while (cursor.hasNext()) {
            Map.Entry<Object, Object> entry = cursor.next();
            String key = (String) entry.getKey();
            String[] split = key.split("::");
            String[] datas = new String[split.length + 1];
            for (int i = 0; i < split.length; i++) {
                datas[i] = split[i];
            }
            String value = String.valueOf(entry.getValue());
            datas[split.length] = value;
            list.add(datas);
        }
        cursor.close();
        return list;
    }

    private List<String[]> obscureSearch(Object mapKey, Object hashKey1, Object hashKey2) {
        Cursor<Map.Entry<Object, Object>> cursor =
                redisTemplate.opsForHash().scan(mapKey,
                        ScanOptions.scanOptions()
                                .match("?" + hashKey1 + "*" + hashKey2 + "?").build());
        List<String[]> list = new ArrayList<>();
        while (cursor.hasNext()) {
            Map.Entry<Object, Object> entry = cursor.next();
            String key = (String) entry.getKey();
            String[] split = key.split("::");
            String[] datas = new String[split.length + 1];
            for (int i = 0; i < split.length; i++) {
                datas[i] = split[i];
            }
            String value = String.valueOf(entry.getValue());
            datas[split.length] = value;
            list.add(datas);
        }
        cursor.close();
        return list;
    }

    @Override
    public boolean isExists(Object mapKey, Object key1, Object key2) {
        String key = RedisKeyUtils.getHashKey(key1, key2);
        boolean isExists = redisTemplate.opsForHash().hasKey(mapKey, key);
        return isExists;
    }

    @Override
    public boolean isExists(Object mapKey, Object key1, Object key2, Object key3) {
        String key = RedisKeyUtils.getHashKey(key1, key2, key3);
        boolean isExists = redisTemplate.opsForHash().hasKey(mapKey, key);
        return isExists;
    }

    /*--- 点赞 ---*/

    @Override
    public void saveLikedToRedis(Long userId, Long beLikedId, Integer type) {
        saveToRedis(RedisKeyUtils.MAP_KEY_USER_LIKED,userId,beLikedId,type,LikedStateEnum.LIKE.getCode());
    }

    @Override
    public void unlikeFromRedis(Long userId, Long beLikedId, Integer type) {
        saveToRedis(RedisKeyUtils.MAP_KEY_USER_LIKED,userId,beLikedId,type,LikedStateEnum.UNLIKE.getCode());
    }

    @Override
    public void deleteLikedFromRedis(Long userId, Long beLikedId, Integer type) {
        deleteFromRedis(RedisKeyUtils.MAP_KEY_USER_LIKED,userId,beLikedId,type);
        deleteFromRedis(RedisKeyUtils.MAP_KEY_OBJECT_LIKED_COUNT,beLikedId,type);
    }

    @Override
    public void deleteLikedFromRedis(Long id, Integer type) {
        deleteBatchFromRedis(RedisKeyUtils.MAP_KEY_USER_LIKED,id,type);
        deleteFromRedis(RedisKeyUtils.MAP_KEY_OBJECT_LIKED_COUNT,id,type);
    }

    @Override
    public void incrementLikedCount(Long beLikedId, Integer type) {
        incrementCount(RedisKeyUtils.MAP_KEY_OBJECT_LIKED_COUNT,beLikedId,type,1);
    }

    @Override
    public void decrementLikedCount(Long beLikedId, Integer type) {
        incrementCount(RedisKeyUtils.MAP_KEY_OBJECT_LIKED_COUNT,beLikedId,type,-1);
    }

    @Override
    public String getLikedStateFromRedis(Long userId, Long beLikedId, Integer type) {
        Object value = getValueFromRedis(RedisKeyUtils.MAP_KEY_USER_LIKED,userId,beLikedId,type);
        return value == null ? null : String.valueOf(value);
    }

    @Override
    public String getLikedCountFromRedis(Long beLikedId, Integer type) {
        Object value = getValueFromRedis(RedisKeyUtils.MAP_KEY_OBJECT_LIKED_COUNT,beLikedId,type);
        return value == null ? null : String.valueOf(value);
    }

    @Override
    public List<Likestatistic> getLikedDataFromRedis() {
        List<Likestatistic> list = new ArrayList<>();
        for (String[] datas : getDataFromRedis(RedisKeyUtils.MAP_KEY_USER_LIKED)) {
            Long userId = Long.valueOf(datas[0]);
            Long beLikedId = Long.valueOf(datas[1]);
            Integer type = Integer.valueOf(datas[2]);
            Integer value = Integer.valueOf(datas[3]);
            Likestatistic likestatistic = new Likestatistic(userId, beLikedId, type, value);
            list.add(likestatistic);
        }
        return list;
    }

    @Override
    public List<Likestatistic> getLikedDataFromRedis(Long userId, Integer type) {
        List<Likestatistic> list = new ArrayList<>();
        for (String[] datas : obscureSearch(RedisKeyUtils.MAP_KEY_USER_LIKED,userId,type)) {
            userId = Long.valueOf(datas[0]);
            Long beLikedId = Long.valueOf(datas[1]);
            type = Integer.valueOf(datas[2]);
            Integer value = Integer.valueOf(datas[3]);
            Likestatistic likestatistic = new Likestatistic(userId, beLikedId, type, value);
            list.add(likestatistic);
        }
        return list;
    }

    @Override
    public List<LikedCountDTO> getLikedCountFromRedis() {
        List<LikedCountDTO> list = new ArrayList<>();
        for (String[] datas : getDataFromRedis(RedisKeyUtils.MAP_KEY_OBJECT_LIKED_COUNT)) {
            Long beLikedId = Long.valueOf(datas[0]);
            Integer type = Integer.valueOf(datas[1]);
            Integer value = Integer.valueOf(datas[2]);
            LikedCountDTO dto = new LikedCountDTO(beLikedId, type, value);
            list.add(dto);
        }
        return list;
    }

    /*--- 收藏 ---*/

    @Override
    public void saveCollectToRedis(Long userId, Long beCollectId, Integer type) {
        saveToRedis(RedisKeyUtils.MAP_KEY_USER_COLLECT,userId,beCollectId,type,CollectStateEnum.COLLECT.getCode());
    }

    @Override
    public void unCollectFromRedis(Long userId, Long beCollectId, Integer type) {
        saveToRedis(RedisKeyUtils.MAP_KEY_USER_COLLECT,userId,beCollectId,type,CollectStateEnum.UNCOLLECT.getCode());
    }

    @Override
    public void deleteCollectFromRedis(Long userId, Long beCollectId, Integer type) {
        deleteFromRedis(RedisKeyUtils.MAP_KEY_USER_COLLECT,userId,beCollectId,type);
        deleteFromRedis(RedisKeyUtils.MAP_KEY_OBJECT_COLLECT_COUNT,beCollectId,type);
    }

    @Override
    public void deleteCollectFromRedis(Long id, Integer type) {
        deleteBatchFromRedis(RedisKeyUtils.MAP_KEY_USER_COLLECT,id,type);
        deleteFromRedis(RedisKeyUtils.MAP_KEY_OBJECT_COLLECT_COUNT,id,type);
    }

    @Override
    public void incrementCollectCount(Long beCollectId, Integer type) {
        incrementCount(RedisKeyUtils.MAP_KEY_OBJECT_COLLECT_COUNT,beCollectId,type,1);
    }

    @Override
    public void decrementCollectCount(Long beCollectId, Integer type) {
        incrementCount(RedisKeyUtils.MAP_KEY_OBJECT_COLLECT_COUNT,beCollectId,type,-1);
    }

    @Override
    public String getCollectStateFromRedis(Long userId, Long beCollectId, Integer type) {
        Object value = getValueFromRedis(RedisKeyUtils.MAP_KEY_USER_COLLECT,userId,beCollectId,type);
        return value == null ? null : String.valueOf(value);
    }

    @Override
    public String getCollectCountFromRedis(Long beCollectId, Integer type) {
        Object value = getValueFromRedis(RedisKeyUtils.MAP_KEY_OBJECT_COLLECT_COUNT,beCollectId,type);
        return value == null ? null : String.valueOf(value);
    }

    @Override
    public List<Collectstatistic> getCollectDataFromRedis() {
        List<Collectstatistic> list = new ArrayList<>();
        for (String[] datas : getDataFromRedis(RedisKeyUtils.MAP_KEY_USER_COLLECT)) {
            Long userId = Long.valueOf(datas[0]);
            Long beCollectId = Long.valueOf(datas[1]);
            Integer type = Integer.valueOf(datas[2]);
            Integer value = Integer.valueOf(datas[3]);
            Collectstatistic collectstatistic = new Collectstatistic(userId, beCollectId, type, value);
            list.add(collectstatistic);
        }
        return list;
    }

    @Override
    public List<Collectstatistic> getCollectDataFromRedis(Long userId, Integer type) {
        List<Collectstatistic> list = new ArrayList<>();
        for (String[] datas : obscureSearch(RedisKeyUtils.MAP_KEY_USER_COLLECT,userId,type)) {
            userId = Long.valueOf(datas[0]);
            Long beCollectId = Long.valueOf(datas[1]);
            type = Integer.valueOf(datas[2]);
            Integer value = Integer.valueOf(datas[3]);
            Collectstatistic collectstatistic = new Collectstatistic(userId, beCollectId, type, value);
            list.add(collectstatistic);
        }
        return list;
    }

    @Override
    public List<CollectCountDTO> getCollectCountFromRedis() {
        List<CollectCountDTO> list = new ArrayList<>();
        for (String[] datas : getDataFromRedis(RedisKeyUtils.MAP_KEY_OBJECT_COLLECT_COUNT)) {
            Long beCollectId = Long.valueOf(datas[0]);
            Integer type = Integer.valueOf(datas[1]);
            Integer value = Integer.valueOf(datas[2]);
            CollectCountDTO dto = new CollectCountDTO(beCollectId, type, value);
            list.add(dto);
        }
        return list;
    }

    /*--- 购物车 ---*/

    @Override
    public void saveShopCartToRedis(Long userId, Long productId, Integer value) {
        saveToRedis(RedisKeyUtils.MAP_KEY_ADD_SHOP_CART,userId,productId,value);
    }

    @Override
    public void deleteShopCartFromRedis(Long userId, Long productId) {
        deleteFromRedis(RedisKeyUtils.MAP_KEY_ADD_SHOP_CART,userId,productId);
    }

    @Override
    public void incrementShopCartCount(Long userId, Long productId) {
        incrementCount(RedisKeyUtils.MAP_KEY_ADD_SHOP_CART,userId,productId,1);
    }

    @Override
    public void decrementShopCartCount(Long userId, Long productId) {
        incrementCount(RedisKeyUtils.MAP_KEY_ADD_SHOP_CART,userId,productId,-1);
    }

    @Override
    public void deleteAllShopCartFromRedis(Long id) {
        deleteBatchFromRedis(RedisKeyUtils.MAP_KEY_ADD_SHOP_CART,id);
    }

    @Override
    public String getShopCartCountFromRedis(Long userId, Long productId) {
        Object value = getValueFromRedis(RedisKeyUtils.MAP_KEY_ADD_SHOP_CART,userId,productId);
        return value == null ? null : String.valueOf(value);
    }

    @Override
    public List<Productshopcart> getShopCartDataFromRedis() {
        List<Productshopcart> list = new ArrayList<>();
        for (String[] datas : getDataFromRedis(RedisKeyUtils.MAP_KEY_ADD_SHOP_CART)) {
            Long userId = Long.valueOf(datas[0]);
            Long productId = Long.valueOf(datas[1]);
            Integer value = Integer.valueOf(datas[2]);
            Productshopcart productshopcart = new Productshopcart(userId, productId, value);
            list.add(productshopcart);
        }
        return list;
    }

    @Override
    public List<Productshopcart> getShopCartDataFromRedis(Long userId) {
        List<Productshopcart> list = new ArrayList<>();
        for (String[] datas : obscureSearch(RedisKeyUtils.MAP_KEY_ADD_SHOP_CART,userId)) {
            userId = Long.valueOf(datas[0]);
            Long productId = Long.valueOf(datas[1]);
            Integer value = Integer.valueOf(datas[2]);
            Productshopcart productshopcart = new Productshopcart(userId, productId, value);
            list.add(productshopcart);
        }
        return list;
    }

    /*--- 浏览记录 ---*/

    @Override
    public void saveBrowseHistoryToRedis(Long userId, Long beBrowseId, String dateTime) {
        saveToRedis(RedisKeyUtils.MAP_KEY_BROWSE_HISTORY,userId,beBrowseId,dateTime);
    }

    @Override
    public void deleteAllBrowseHistoryFromRedis(Long id) {
        deleteBatchFromRedis(RedisKeyUtils.MAP_KEY_BROWSE_HISTORY,id);
    }

    @SneakyThrows
    @Override
    public List<Browsinghistory> getBrowseHistoryDataFromRedis() {
        List<Browsinghistory> list = new ArrayList<>();
        for (String[] datas : getDataFromRedis(RedisKeyUtils.MAP_KEY_BROWSE_HISTORY)) {
            Long userId = Long.valueOf(datas[0]);
            Long beBrowseId = Long.valueOf(datas[1]);
            Date dateTime = new Date(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(String.valueOf(datas[2])).getTime());
            Browsinghistory browsinghistory = new Browsinghistory(userId, beBrowseId, dateTime);
            list.add(browsinghistory);
        }
        return list;
    }

    @SneakyThrows
    @Override
    public List<Browsinghistory> getBrowseHistoryDataFromRedis(Long userId) {
        List<Browsinghistory> list = new ArrayList<>();
        for (String[] datas : obscureSearch(RedisKeyUtils.MAP_KEY_BROWSE_HISTORY,userId)) {
            userId = Long.valueOf(datas[0]);
            Long beBrowseId = Long.valueOf(datas[1]);
            Date dateTime = new Date(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(String.valueOf(datas[2])).getTime());
            Browsinghistory browsinghistory = new Browsinghistory(userId, beBrowseId, dateTime);
            list.add(browsinghistory);
            deleteFromRedis(RedisKeyUtils.MAP_KEY_BROWSE_HISTORY,userId,beBrowseId);
        }
        return list;
    }

}
