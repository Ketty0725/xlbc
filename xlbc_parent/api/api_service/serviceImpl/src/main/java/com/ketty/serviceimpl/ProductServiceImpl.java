package com.ketty.serviceimpl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.ketty.api_entity.*;
import com.ketty.api_entity.enums.CollectTypeEnum;
import com.ketty.api_mapper.ProductMapper;
import com.ketty.common_base.MinioUtil;
import com.ketty.common_utils.StringUtils;
import com.ketty.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ketty
 * @since 2023-02-14
 */
@Service
@DS("master")
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {
    @Autowired
    ProductMapper productMapper;
    @Autowired
    CollectstatisticService collectstatisticService;
    @Autowired
    RedisService redisService;
    @Autowired
    SysOssService sysOssService;

    @Override
    public Product getById(Long id) {
        Product product = productMapper.selectById(id);
        String ossId = product.getImage();
        if (StringUtils.isNotBlank(ossId)) {
            SysOss oss = sysOssService.getById(Long.valueOf(ossId));
            product.setImage(oss.getUrl());
        }
        return product;
    }

    @Override
    public int getInventory(Long id) {
        Product product = productMapper.selectOne(
                new LambdaQueryWrapper<Product>()
                        .select(Product::getInventory)
                        .eq(Product::getId,id));
        return product.getInventory();
    }

    @Override
    public List<Product> getItemList(Long categoryId) {
        List<Product> list = productMapper.selectList(
                new LambdaQueryWrapper<Product>()
                        .select(Product::getId,Product::getName,Product::getImage,Product::getPrice,Product::getInventory)
                        .eq(Product::getCategoryId,categoryId));
        if (list.size() > 0) {
            for (Product product : list) {
                String ossId = product.getImage();
                if (StringUtils.isNotBlank(ossId)) {
                    SysOss oss = sysOssService.getById(Long.valueOf(ossId));
                    product.setImage(oss.getUrl());
                }
            }
        }
        return list;
    }

    @Override
    public List<Product> getByIds(List<Long> ids) {
        List<Product> result = new ArrayList<>();
        for (Long id : ids) {
            Product product = getById(id);
            result.add(product);
        }
        return result;
    }

    @Override
    public List<Product> selectLikeName(String name) {
        List<Product> list = productMapper.selectList(
                new LambdaQueryWrapper<Product>()
                        .select(Product::getId,Product::getName,Product::getImage,Product::getPrice)
                        .like(name != null, Product::getName,name)
                        .orderByAsc(Product::getId));
        if (list.size() > 0) {
            for (Product product : list) {
                String ossId = product.getImage();
                if (StringUtils.isNotBlank(ossId)) {
                    SysOss oss = sysOssService.getById(Long.valueOf(ossId));
                    product.setImage(oss.getUrl());
                }
            }
            return list;
        } else {
            throw new RuntimeException("无数据");
        }
    }

    @Override
    public void updateCollectState(Collectstatistic entity) {
        collectstatisticService.saveToRedis(entity.getUserId(),entity.getBeCollectId(), CollectTypeEnum.PRODUCT.getCode());
    }

    @Override
    public void updateCollectCount(CollectCountDTO dto) {
        productMapper.update(null, new LambdaUpdateWrapper<Product>()
                .eq(Product::getId,dto.getBeCollectId())
                .setSql("collect_count = collect_count + " + dto.getCount()));
    }

    @Override
    public int getIsCollectByUserId(Long userId, Long id) {
        int collectState = collectstatisticService.getState(userId, id, CollectTypeEnum.PRODUCT.getCode());
        return collectState;
    }

    @Override
    public List<Product> getBeCollectList(Long userId) {
        List<Collectstatistic> list = collectstatisticService.getBeCollectByUser(userId,CollectTypeEnum.PRODUCT.getCode());
        List<Long> ids = new ArrayList<>();
        for (Collectstatistic collect : list) {
            ids.add(collect.getBeCollectId());
        }
        List<Product> result = getByIds(ids);
        return result;
    }

    @Override
    public void updateInventory(Long id, Integer number) {
        productMapper.update(null, new LambdaUpdateWrapper<Product>()
                .eq(Product::getId,id)
                .setSql("inventory = inventory - " + number));
    }

}
