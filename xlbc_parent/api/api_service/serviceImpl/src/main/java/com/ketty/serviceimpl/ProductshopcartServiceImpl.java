package com.ketty.serviceimpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.ketty.api_entity.Productshopcart;
import com.ketty.api_mapper.ProductshopcartMapper;
import com.ketty.common_utils.RedisKeyUtils;
import com.ketty.service.ProductshopcartService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ketty.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ketty
 * @since 2023-02-20
 */
@Service
public class ProductshopcartServiceImpl extends ServiceImpl<ProductshopcartMapper, Productshopcart> implements ProductshopcartService {
    @Autowired
    ProductshopcartMapper productshopcartMapper;
    @Autowired
    RedisService redisService;

    @Override
    public boolean isExistsFromDB(Long userId, Long productId) {
        boolean exists = productshopcartMapper.exists(
                new LambdaQueryWrapper<Productshopcart>()
                        .eq(Productshopcart::getUserId, userId)
                        .eq(Productshopcart::getProductId, productId));
        return exists;
    }

    @Override
    public boolean isExistsFromAll(Long userId, Long productId) {
        boolean existsFromDB = isExistsFromDB(userId, productId);
        String value = redisService.getShopCartCountFromRedis(userId,productId);
        if (value == null || "".equals(value)) {
            if (existsFromDB) {
                return true;
            }
        } else {
            if (!"0".equals(value)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Productshopcart selectOne(Long userId, Long productId) {
        Productshopcart bean = productshopcartMapper.selectOne(
                new LambdaQueryWrapper<Productshopcart>()
                .eq(Productshopcart::getUserId, userId)
                .eq(Productshopcart::getProductId, productId));
        return bean;
    }

    @Override
    @Transactional
    public void addOrDelete(Productshopcart bean) {
        if (bean != null) {
            Long userId = bean.getUserId();
            Long productId = bean.getProductId();
            Integer number = bean.getNumber();
            if (number > 0) {
                if (selectOne(userId, productId) != null) {
                    productshopcartMapper.update(null, new LambdaUpdateWrapper<Productshopcart>()
                            .eq(Productshopcart::getUserId, userId)
                            .eq(Productshopcart::getProductId, productId)
                            .set(Productshopcart::getNumber, number));
                } else {
                    productshopcartMapper.insert(bean);
                }
            } else {
                productshopcartMapper.delete(
                        new LambdaUpdateWrapper<Productshopcart>()
                                .eq(Productshopcart::getUserId, userId)
                                .eq(Productshopcart::getProductId, productId));
            }
        }
    }

    @Override
    @Transactional
    public void addOrIncrementToRedis(Long userId, Long productId) {
        String value = redisService.getShopCartCountFromRedis(userId,productId);
        if (value == null) {
            Productshopcart bean = selectOne(userId, productId);
            if (bean == null) {
                redisService.saveShopCartToRedis(userId,productId,1);
            } else {
                redisService.saveShopCartToRedis(userId,productId,bean.getNumber());
                redisService.incrementShopCartCount(userId,productId);
            }
        } else {
            redisService.incrementShopCartCount(userId,productId);
        }
    }

    @Override
    @Transactional
    public void decrementToRedis(Long userId, Long productId) {
        String value = redisService.getShopCartCountFromRedis(userId,productId);
        if (value == null) {
            Productshopcart bean = selectOne(userId, productId);
            if (bean != null) {
                redisService.saveShopCartToRedis(userId,productId,bean.getNumber());
                redisService.decrementShopCartCount(userId,productId);
            }
        } else {
            redisService.decrementShopCartCount(userId,productId);
        }
    }

    @Override
    @Transactional
    public void deleteToRedis(List<Productshopcart> list) {
        for (Productshopcart cart : list) {
            Long userId = cart.getUserId();
            Long productId = cart.getProductId();
            if (isExistsFromDB(userId,productId)) {
                redisService.saveShopCartToRedis(userId,productId,0);
            } else {
                redisService.deleteShopCartFromRedis(userId,productId);
            }
        }
    }

    @Override
    @Transactional
    public void transShopCartFromRedis2DB() {
        List<Productshopcart> list = redisService.getShopCartDataFromRedis();
        if (list != null && list.size() > 0) {
            for (Productshopcart bean : list) {
                addOrDelete(bean);
            }
        }
    }

    @Override
    public List<Productshopcart> getByUserId(Long userId) {
        List<Productshopcart> dbList = productshopcartMapper.selectList(
                new LambdaQueryWrapper<Productshopcart>()
                        .eq(Productshopcart::getUserId,userId));
        List<Productshopcart> rdList = redisService.getShopCartDataFromRedis(userId);
        if (dbList.size() > 0 && rdList.size() > 0) {
            for (Iterator<Productshopcart> iterator = rdList.iterator(); iterator.hasNext();) {
                Productshopcart bean = iterator.next();
                if (bean.getNumber() == 0) {
                    iterator.remove();
                    dbList.removeIf(next -> next.getUserId().equals(bean.getUserId())
                            && next.getProductId().equals(bean.getProductId()));
                } else {
                    dbList.removeIf(next -> next.getUserId().equals(bean.getUserId())
                            && next.getProductId().equals(bean.getProductId()));
                }
            }
            dbList.addAll(rdList);
            return dbList;
        } else if (dbList.size() > 0) {
            return dbList;
        } else if (rdList.size() > 0) {
            rdList.removeIf(next -> next.getNumber() == 0);
            return rdList;
        }
        return null;
    }

    @Override
    public void deleteByUser(Long userId) {
        redisService.deleteAllShopCartFromRedis(userId);
        productshopcartMapper.delete(new LambdaUpdateWrapper<Productshopcart>()
                .eq(Productshopcart::getUserId,userId));
    }

}
