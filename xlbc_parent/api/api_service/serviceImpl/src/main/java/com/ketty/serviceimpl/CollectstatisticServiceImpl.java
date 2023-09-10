package com.ketty.serviceimpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.ketty.api_entity.CollectCountDTO;
import com.ketty.api_entity.Collectstatistic;
import com.ketty.api_entity.LikedCountDTO;
import com.ketty.api_entity.Likestatistic;
import com.ketty.api_entity.enums.CollectTypeEnum;
import com.ketty.api_entity.enums.LikedTypeEnum;
import com.ketty.api_mapper.CollectstatisticMapper;
import com.ketty.api_mapper.LikestatisticMapper;
import com.ketty.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
 * @since 2023-02-27
 */
@Service
public class CollectstatisticServiceImpl extends ServiceImpl<CollectstatisticMapper, Collectstatistic> implements CollectstatisticService {
    @Autowired
    CollectstatisticMapper collectMapper;
    @Autowired
    RedisService redisService;
    @Autowired
    CommunityService communityService;
    @Autowired
    ProductService productService;

    @Override
    public boolean isCollect(Long userId, Long beCollectId, Integer type) {
        Collectstatistic collect = selectOne(userId, beCollectId, type);
        if (collect != null) {
            return true;
        }
        return false;
    }

    @Override
    public Collectstatistic selectOne(Long userId, Long beCollectId, Integer type) {
        Collectstatistic collect = collectMapper.selectOne(new LambdaQueryWrapper<Collectstatistic>()
                .eq(Collectstatistic::getUserId, userId)
                .eq(Collectstatistic::getBeCollectId, beCollectId)
                .eq(Collectstatistic::getType, type));
        return collect;
    }

    @Override
    public List<Collectstatistic> selectList(Long userId, Integer type) {
        List<Collectstatistic> list = collectMapper.selectList(new LambdaQueryWrapper<Collectstatistic>()
                .eq(Collectstatistic::getUserId, userId)
                .eq(Collectstatistic::getType, type));
        return list;
    }

    @Override
    public List<Collectstatistic> getBeCollectByUser(Long userId, Integer type) {
        List<Collectstatistic> rdList = redisService.getCollectDataFromRedis(userId,type);
        List<Collectstatistic> dbList = selectList(userId,type);
        if (rdList.size() > 0) {
            if (dbList.size() > 0) {
                for (Collectstatistic rdCollect : rdList) {
                    dbList.removeIf(dbCollect -> rdCollect.getBeCollectId().equals(dbCollect.getBeCollectId()));
                }
                rdList.removeIf(collect -> collect.getState() == 0);
                dbList.removeIf(collect -> collect.getState() == 0);
                rdList.addAll(dbList);
            }
            return rdList;
        } else {
            dbList.removeIf(collect -> collect.getState() == 0);
            return dbList;
        }
    }

    @Override
    public int getState(Long userId, Long beCollectId, Integer type) {
        String value = redisService.getCollectStateFromRedis(userId,beCollectId,type);
        if (value == null) {
            Collectstatistic collect = selectOne(userId, beCollectId, type);
            return collect == null ? 0 : collect.getState();
        } else {
            return Integer.parseInt(value);
        }
    }

    @Override
    @Transactional
    public void updateCollectState(Collectstatistic entity) {
        if (entity != null) {
            Long userId = entity.getUserId();
            Long beCollectId = entity.getBeCollectId();
            Integer type = entity.getType();
            Collectstatistic collect = selectOne(userId, beCollectId, type);
            if (collect != null) {
                collectMapper.update(null, new LambdaUpdateWrapper<Collectstatistic>()
                        .eq(Collectstatistic::getUserId, userId)
                        .eq(Collectstatistic::getBeCollectId, beCollectId)
                        .eq(Collectstatistic::getType, type)
                        .set(Collectstatistic::getState, entity.getState()));
            } else {
                collectMapper.insert(entity);
            }
        }
    }

    @Override
    @Transactional
    public void updateCollectCount(CollectCountDTO entity) {
        if (entity != null) {
            Integer type = entity.getType();
            switch (Objects.requireNonNull(CollectTypeEnum.getByValue(type))) {
                case NOTE:
                    communityService.updateCollectCount(entity);
                    break;
                case PRODUCT:
                    productService.updateCollectCount(entity);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void deleteBatch(Long beCollectId, Integer type) {
        redisService.deleteCollectFromRedis(beCollectId,type);
        collectMapper.delete(new LambdaUpdateWrapper<Collectstatistic>()
                .eq(Collectstatistic::getBeCollectId,beCollectId)
                .eq(Collectstatistic::getType,type));
    }

    @Override
    @Transactional
    public void saveToRedis(Long userId, Long beCollectId, Integer type) {
        String value = redisService.getCollectStateFromRedis(userId,beCollectId,type);
        if (value == null) {
            Collectstatistic collect = selectOne(userId, beCollectId, type);
            if (collect == null || collect.getState() == 0) {
                redisService.saveCollectToRedis(userId,beCollectId,type);
                redisService.incrementCollectCount(beCollectId,type);
            } else {
                redisService.unCollectFromRedis(userId,beCollectId,type);
                redisService.decrementCollectCount(beCollectId,type);
            }
        } else if (StringUtils.equals(value,"1")){
            redisService.unCollectFromRedis(userId,beCollectId,type);
            redisService.decrementCollectCount(beCollectId,type);
        } else {
            redisService.saveCollectToRedis(userId,beCollectId,type);
            redisService.incrementCollectCount(beCollectId,type);
        }
    }

    @Override
    @Transactional
    public void transCollectFromRedis2DB() {
        List<Collectstatistic> list = redisService.getCollectDataFromRedis();
        if (list != null && list.size() > 0) {
            for (Collectstatistic collect : list) {
                updateCollectState(collect);
            }
        }
    }

    @Override
    @Transactional
    public void transCollectCountFromRedis2DB() {
        List<CollectCountDTO> list = redisService.getCollectCountFromRedis();
        if (list != null && list.size() > 0) {
            for (CollectCountDTO collect : list) {
                updateCollectCount(collect);
            }
        }
    }
}
