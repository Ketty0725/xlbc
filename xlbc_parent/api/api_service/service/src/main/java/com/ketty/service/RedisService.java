package com.ketty.service;

import com.ketty.api_entity.*;

import java.util.List;

/**
 * @Auther: Ketty Allen
 * @Date:2023/2/2 - 23:39
 * @Description:com.ketty.api_service
 * @version: 1.0
 */
public interface RedisService {

    boolean isExists(Object mapKey, Object key1, Object key2);

    boolean isExists(Object mapKey, Object key1, Object key2, Object key3);

    /*--- 点赞 ---*/

    /**
     * 点赞。状态为1
     * @param userId
     * @param beLikedId
     */
    void saveLikedToRedis(Long userId, Long beLikedId, Integer type);

    /**
     * 取消点赞。将状态改变为0
     * @param userId
     * @param beLikedId
     */
    void unlikeFromRedis(Long userId, Long beLikedId, Integer type);

    /**
     * 从Redis中删除一条点赞数据
     * @param userId
     * @param beLikedId
     */
    void deleteLikedFromRedis(Long userId, Long beLikedId, Integer type);

    /**
     * 从Redis中删除用户或者被点赞对象的所有数据
     * @param id
     */
    void deleteLikedFromRedis(Long id, Integer type);

    /**
     * 该对象的点赞数加1
     * @param beLikedId
     * @param type
     */
    void incrementLikedCount(Long beLikedId, Integer type);

    /**
     * 该对象的点赞数减1
     * @param beLikedId
     * @param type
     */
    void decrementLikedCount(Long beLikedId, Integer type);

    /**
     * 从Redis中获取点赞状态
     * @param userId
     * @param beLikedId
     */
    String getLikedStateFromRedis(Long userId, Long beLikedId, Integer type);

    /**
     * 从Redis中获取点赞数量
     * @param beLikedId
     * @param type
     */
    String getLikedCountFromRedis(Long beLikedId, Integer type);

    /**
     * 获取Redis中存储的所有点赞数据
     * @return
     */
    List<Likestatistic> getLikedDataFromRedis();

    List<Likestatistic> getLikedDataFromRedis(Long userId, Integer type);

    /**
     * 获取Redis中存储的所有点赞数量
     * @return
     */
    List<LikedCountDTO> getLikedCountFromRedis();

    /*--- 收藏 ---*/

    /**
     * 收藏。状态为1
     * @param userId
     * @param beCollectId
     */
    void saveCollectToRedis(Long userId, Long beCollectId, Integer type);

    /**
     * 取消收藏。将状态改变为0
     * @param userId
     * @param beCollectId
     */
    void unCollectFromRedis(Long userId, Long beCollectId, Integer type);

    /**
     * 从Redis中删除一条收藏数据
     * @param userId
     * @param beCollectId
     */
    void deleteCollectFromRedis(Long userId, Long beCollectId, Integer type);

    /**
     * 从Redis中删除用户或者被收藏对象的所有数据
     * @param id
     */
    void deleteCollectFromRedis(Long id, Integer type);

    /**
     * 该对象的收藏数加1
     * @param beCollectId
     * @param type
     */
    void incrementCollectCount(Long beCollectId, Integer type);

    /**
     * 该对象的收藏数减1
     * @param beCollectId
     * @param type
     */
    void decrementCollectCount(Long beCollectId, Integer type);

    /**
     * 从Redis中获取收藏状态
     * @param userId
     * @param beCollectId
     */
    String getCollectStateFromRedis(Long userId, Long beCollectId, Integer type);

    /**
     * 从Redis中获取收藏数量
     * @param beCollectId
     * @param type
     */
    String getCollectCountFromRedis(Long beCollectId, Integer type);

    /**
     * 获取Redis中存储的所有收藏数据
     * @return
     */
    List<Collectstatistic> getCollectDataFromRedis();

    List<Collectstatistic> getCollectDataFromRedis(Long userId, Integer type);

    /**
     * 获取Redis中存储的所有收藏数量
     * @return
     */
    List<CollectCountDTO> getCollectCountFromRedis();

    /*--- 购物车 ---*/

    /**
     * 添加商品
     * @param userId
     * @param productId
     */
    void saveShopCartToRedis(Long userId, Long productId, Integer value);

    /**
     * 从Redis中删除一条商品数据
     * @param userId
     * @param productId
     */
    void deleteShopCartFromRedis(Long userId, Long productId);

    /**
     * 该商品的加购数量加1
     * @param userId
     * @param productId
     */
    void incrementShopCartCount(Long userId, Long productId);

    /**
     * 该商品的加购数量减1
     * @param userId
     * @param productId
     */
    void decrementShopCartCount(Long userId, Long productId);

    /**
     * 从Redis中清空某个用户或者商品的所有数据
     * @param id
     */
    void deleteAllShopCartFromRedis(Long id);

    /**
     * 从Redis中获取商品数量
     * @param userId
     * @param productId
     */
    String getShopCartCountFromRedis(Long userId, Long productId);

    /**
     * 获取Redis中存储的所有购物车商品数据
     * @return
     */
    List<Productshopcart> getShopCartDataFromRedis();

    /**
     * 获取Redis中存储的某个用户的购物车商品数据
     * @return
     */
    List<Productshopcart> getShopCartDataFromRedis(Long userId);

    /*--- 浏览记录 ---*/

    /**
     * 添加浏览记录
     * @param userId
     * @param beBrowseId
     */
    void saveBrowseHistoryToRedis(Long userId, Long beBrowseId, String dateTime);

    /**
     * 从Redis中清空某个用户或者被浏览对象的所有数据
     * @param id
     */
    void deleteAllBrowseHistoryFromRedis(Long id);

    /**
     * 获取Redis中存储的所有浏览记录数据
     * @return
     */
    List<Browsinghistory> getBrowseHistoryDataFromRedis();

    /**
     * 获取Redis中存储的某个用户的浏览记录数据
     * @return
     */
    List<Browsinghistory> getBrowseHistoryDataFromRedis(Long userId);

}
