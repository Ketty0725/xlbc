package com.ketty.serviceimpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ketty.api_entity.Browsinghistory;
import com.ketty.api_entity.Productshopcart;
import com.ketty.api_entity.Reply;
import com.ketty.api_mapper.BrowsinghistoryMapper;
import com.ketty.service.BrowsinghistoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ketty.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ketty
 * @since 2023-03-04
 */
@Service
public class BrowsinghistoryServiceImpl extends ServiceImpl<BrowsinghistoryMapper, Browsinghistory> implements BrowsinghistoryService {
    @Autowired
    BrowsinghistoryMapper mapper;
    @Autowired
    RedisService redisService;

    @Override
    public boolean isExistsFromDB(Long userId, Long beBrowseId) {
        boolean exists = mapper.exists(
                new LambdaQueryWrapper<Browsinghistory>()
                        .eq(Browsinghistory::getUserId, userId)
                        .eq(Browsinghistory::getBeBrowseId, beBrowseId));
        return exists;
    }

    @Override
    public Browsinghistory selectOne(Long userId, Long beBrowseId) {
        Browsinghistory bean = mapper.selectOne(
                new LambdaQueryWrapper<Browsinghistory>()
                        .eq(Browsinghistory::getUserId, userId)
                        .eq(Browsinghistory::getBeBrowseId, beBrowseId));
        return bean;
    }

    @Override
    public void addToDatabase(Browsinghistory bean) {
        if (bean != null) {
            Long userId = bean.getUserId();
            Long beBrowseId = bean.getBeBrowseId();
            Date browseTime = bean.getBrowseTime();
            if (isExistsFromDB(userId, beBrowseId)) {
                mapper.update(null, new LambdaUpdateWrapper<Browsinghistory>()
                        .eq(Browsinghistory::getUserId, userId)
                        .eq(Browsinghistory::getBeBrowseId, beBrowseId)
                        .set(Browsinghistory::getBrowseTime, browseTime));
            } else {
                mapper.insert(bean);
            }
        }
    }

    @Override
    @Transactional
    public void addOrUpdateToRedis(Long userId, Long beBrowseId) {
        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        redisService.saveBrowseHistoryToRedis(userId, beBrowseId, time);
    }

    @Override
    @Transactional
    public void deleteAllByUser(Long userId) {
        redisService.deleteAllBrowseHistoryFromRedis(userId);
        mapper.delete(new LambdaUpdateWrapper<Browsinghistory>()
                .eq(Browsinghistory::getUserId, userId));
    }

    @Override
    public void deleteAllByObject(Long beBrowseId) {
        redisService.deleteAllBrowseHistoryFromRedis(beBrowseId);
        mapper.delete(new LambdaUpdateWrapper<Browsinghistory>()
                .eq(Browsinghistory::getBeBrowseId, beBrowseId));
    }

    @Override
    public IPage<Browsinghistory> getByUserId(Long userId, Long currentPage) {
        if (currentPage == 1) {
            List<Browsinghistory> rdList = redisService.getBrowseHistoryDataFromRedis(userId);
            if (rdList != null && rdList.size() > 0) {
                for (Browsinghistory bean : rdList) {
                    addToDatabase(bean);
                }
            }
        }
        Page<Browsinghistory> page = new Page<>(currentPage, 10);
        LambdaQueryWrapper<Browsinghistory> wrapper = new LambdaQueryWrapper<Browsinghistory>()
                .eq(Browsinghistory::getUserId,userId)
                .orderByDesc(Browsinghistory::getBrowseTime);
        IPage<Browsinghistory> iPage = mapper.selectPage(page, wrapper);
        return iPage;
    }

    @Override
    @Transactional
    public void transBrowseHistoryFromRedis2DB() {
        List<Browsinghistory> list = redisService.getBrowseHistoryDataFromRedis();
        if (list != null && list.size() > 0) {
            for (Browsinghistory bean : list) {
                addToDatabase(bean);
            }
        }
    }
}
