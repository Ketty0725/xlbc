package com.ketty.service;

import com.ketty.api_entity.LikedCountDTO;
import com.ketty.api_entity.Likestatistic;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ketty
 * @since 2023-02-01
 */
public interface LikestatisticService extends IService<Likestatistic> {
    boolean isLiked(Long userId, Long beLikedId, Integer type);

    Likestatistic selectOne(Long userId, Long beLikedId, Integer type);

    List<Likestatistic> selectList(Long userId, Integer type);

    List<Likestatistic> getBeLikedByUser(Long userId, Integer type);

    int getState(Long userId, Long beLikedId, Integer type);

    void delete(Long userId, Long beLikedId, Integer type);

    void deleteBatch(Long beLikedId, Integer type);

    void updateLikedState(Likestatistic entity);

    void updateLikedCount(LikedCountDTO entity);

    void saveToRedis(Long userId, Long beLikedId, Integer type);

    /**
     * 将Redis里的点赞数据存入数据库中
     */
    void transLikedFromRedis2DB();

    /**
     * 将Redis中的点赞数量数据存入数据库
     */
    void transLikedCountFromRedis2DB();

}
