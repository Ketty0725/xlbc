package com.ketty.service;

import com.ketty.api_entity.Productshopcart;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ketty
 * @since 2023-02-20
 */
public interface ProductshopcartService extends IService<Productshopcart> {

    boolean isExistsFromDB(Long userId, Long productId);

    boolean isExistsFromAll(Long userId, Long productId);

    Productshopcart selectOne(Long userId, Long productId);

    void addOrDelete(Productshopcart bean);

    void addOrIncrementToRedis(Long userId, Long productId);

    void decrementToRedis(Long userId, Long productId);

    void deleteToRedis(List<Productshopcart> list);

    void transShopCartFromRedis2DB();

    List<Productshopcart> getByUserId(Long userId);

    void deleteByUser(Long userId);

}
