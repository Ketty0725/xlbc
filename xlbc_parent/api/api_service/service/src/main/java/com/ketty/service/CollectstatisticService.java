package com.ketty.service;

import com.ketty.api_entity.CollectCountDTO;
import com.ketty.api_entity.Collectstatistic;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ketty.api_entity.LikedCountDTO;
import com.ketty.api_entity.Likestatistic;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ketty
 * @since 2023-02-27
 */
public interface CollectstatisticService extends IService<Collectstatistic> {
    boolean isCollect(Long userId, Long beCollectId, Integer type);

    Collectstatistic selectOne(Long userId, Long beCollectId, Integer type);

    List<Collectstatistic> selectList(Long userId, Integer type);

    List<Collectstatistic> getBeCollectByUser(Long userId, Integer type);

    int getState(Long userId, Long beCollectId, Integer type);

    void updateCollectState(Collectstatistic entity);

    void updateCollectCount(CollectCountDTO entity);

    void deleteBatch(Long beCollectId, Integer type);

    void saveToRedis(Long userId, Long beCollectId, Integer type);

    /**
     * 将Redis里的收藏数据存入数据库中
     */
    void transCollectFromRedis2DB();

    /**
     * 将Redis中的收藏数量数据存入数据库
     */
    void transCollectCountFromRedis2DB();

}
