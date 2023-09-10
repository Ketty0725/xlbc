package com.ketty.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ketty.api_entity.Browsinghistory;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ketty.api_entity.Productshopcart;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ketty
 * @since 2023-03-04
 */
public interface BrowsinghistoryService extends IService<Browsinghistory> {

    boolean isExistsFromDB(Long userId, Long beBrowseId);

    Browsinghistory selectOne(Long userId, Long beBrowseId);

    void addToDatabase(Browsinghistory bean);

    void addOrUpdateToRedis(Long userId, Long beBrowseId);

    void deleteAllByUser(Long userId);

    void deleteAllByObject(Long beBrowseId);

    IPage<Browsinghistory> getByUserId(Long userId, Long currentPage);

    void transBrowseHistoryFromRedis2DB();

}
