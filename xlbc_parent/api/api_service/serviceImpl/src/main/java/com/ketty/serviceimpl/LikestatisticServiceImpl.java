package com.ketty.serviceimpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.ketty.api_entity.LikedCountDTO;
import com.ketty.api_entity.Likestatistic;
import com.ketty.api_entity.enums.LikedTypeEnum;
import com.ketty.api_mapper.LikestatisticMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ketty.common_utils.RedisKeyUtils;
import com.ketty.service.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ketty
 * @since 2023-02-01
 */
@Service
public class LikestatisticServiceImpl extends ServiceImpl<LikestatisticMapper, Likestatistic> implements LikestatisticService {
    @Autowired
    LikestatisticMapper likeMapper;
    @Autowired
    RedisService redisService;
    @Autowired
    CommentService commentService;
    @Autowired
    ReplyService replyService;
    @Autowired
    CommunityService communityService;

    @Override
    public boolean isLiked(Long userId, Long beLikedId, Integer type) {
        Likestatistic like = selectOne(userId, beLikedId, type);
        if (like != null) {
            return true;
        }
        return false;
    }

    @Override
    public Likestatistic selectOne(Long userId, Long beLikedId, Integer type) {
        Likestatistic like = likeMapper.selectOne(new LambdaQueryWrapper<Likestatistic>()
                .eq(Likestatistic::getUserId, userId)
                .eq(Likestatistic::getBeLikedId, beLikedId)
                .eq(Likestatistic::getType, type));
        return like;
    }

    @Override
    public List<Likestatistic> selectList(Long userId, Integer type) {
        List<Likestatistic> list = likeMapper.selectList(new LambdaQueryWrapper<Likestatistic>()
                .eq(Likestatistic::getUserId, userId)
                .eq(Likestatistic::getType, type));
        return list;
    }

    @Override
    public List<Likestatistic> getBeLikedByUser(Long userId, Integer type) {
        List<Likestatistic> rdList = redisService.getLikedDataFromRedis(userId,type);
        List<Likestatistic> dbList = selectList(userId,type);
        if (rdList.size() > 0) {
            if (dbList.size() > 0) {
                for (Likestatistic rdLike : rdList) {
                    dbList.removeIf(dbLike -> rdLike.getBeLikedId().equals(dbLike.getBeLikedId()));
                }
                rdList.removeIf(like -> like.getState() == 0);
                dbList.removeIf(like -> like.getState() == 0);
                rdList.addAll(dbList);
            }
            return rdList;
        } else {
            dbList.removeIf(like -> like.getState() == 0);
            return dbList;
        }
    }

    @Override
    public int getState(Long userId, Long beLikedId, Integer type) {
        String value = redisService.getLikedStateFromRedis(userId,beLikedId,type);
        if (value == null) {
            Likestatistic like = selectOne(userId, beLikedId, type);
            return like == null ? 0 : like.getState();
        } else {
            return Integer.parseInt(value);
        }
    }

    @Override
    public void delete(Long userId, Long beLikedId, Integer type) {
        boolean isExists = redisService.isExists(RedisKeyUtils.MAP_KEY_USER_LIKED,userId,beLikedId,type);
        if (isExists) {
            redisService.deleteLikedFromRedis(userId,beLikedId,type);
        }
        likeMapper.delete(new LambdaUpdateWrapper<Likestatistic>()
                .eq(Likestatistic::getUserId,userId)
                .eq(Likestatistic::getBeLikedId,beLikedId)
                .eq(Likestatistic::getType,type));
    }

    @Override
    public void deleteBatch(Long beLikedId, Integer type) {
        redisService.deleteLikedFromRedis(beLikedId,type);
        likeMapper.delete(new LambdaUpdateWrapper<Likestatistic>()
                .eq(Likestatistic::getBeLikedId,beLikedId)
                .eq(Likestatistic::getType,type));
    }

    @Override
    @Transactional
    public void updateLikedState(Likestatistic entity) {
        if (entity != null) {
            Long userId = entity.getUserId();
            Long beLikedId = entity.getBeLikedId();
            Integer type = entity.getType();
            Likestatistic like = selectOne(userId, beLikedId, type);
            if (like != null) {
                likeMapper.update(null, new LambdaUpdateWrapper<Likestatistic>()
                        .eq(Likestatistic::getUserId, userId)
                        .eq(Likestatistic::getBeLikedId, beLikedId)
                        .eq(Likestatistic::getType, type)
                        .set(Likestatistic::getState, entity.getState()));
            } else {
                likeMapper.insert(entity);
            }
        }
    }

    @Override
    @Transactional
    public void updateLikedCount(LikedCountDTO entity) {
        if (entity != null) {
            Integer type = entity.getType();
            switch (Objects.requireNonNull(LikedTypeEnum.getByValue(type))) {
                case COMMENT:
                    commentService.updateLikedCount(entity);
                    break;
                case REPLY:
                    replyService.updateLikedCount(entity);
                    break;
                case NOTE:
                    communityService.updateLikedCount(entity);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    @Transactional
    public void saveToRedis(Long userId, Long beLikedId, Integer type) {
        String value = redisService.getLikedStateFromRedis(userId,beLikedId,type);
        if (value == null) {
            Likestatistic like = selectOne(userId, beLikedId, type);
            if (like == null || like.getState() == 0) {
                redisService.saveLikedToRedis(userId,beLikedId,type);
                redisService.incrementLikedCount(beLikedId,type);
            } else {
                redisService.unlikeFromRedis(userId,beLikedId,type);
                redisService.decrementLikedCount(beLikedId,type);
            }
        } else if (StringUtils.equals(value,"1")){
            redisService.unlikeFromRedis(userId,beLikedId,type);
            redisService.decrementLikedCount(beLikedId,type);
        } else {
            redisService.saveLikedToRedis(userId,beLikedId,type);
            redisService.incrementLikedCount(beLikedId,type);
        }
    }

    @Override
    @Transactional
    public void transLikedFromRedis2DB() {
        List<Likestatistic> list = redisService.getLikedDataFromRedis();
        if (list != null && list.size() > 0) {
            for (Likestatistic like : list) {
                updateLikedState(like);
            }
        }
    }

    @Override
    @Transactional
    public void transLikedCountFromRedis2DB() {
        List<LikedCountDTO> list = redisService.getLikedCountFromRedis();
        if (list != null && list.size() > 0) {
            for (LikedCountDTO like : list) {
                updateLikedCount(like);
            }
        }
    }

}
