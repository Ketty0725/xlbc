package com.ketty.serviceimpl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ketty.api_entity.Chineseherbcategory;
import com.ketty.api_entity.Productcategory;
import com.ketty.api_entity.SysOss;
import com.ketty.api_mapper.ProductcategoryMapper;
import com.ketty.common_utils.StringUtils;
import com.ketty.service.ProductcategoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ketty.service.SysOssService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ketty
 * @since 2023-04-05
 */
@Service
@DS("master")
public class ProductcategoryServiceImpl extends ServiceImpl<ProductcategoryMapper, Productcategory> implements ProductcategoryService {
    @Autowired
    ProductcategoryMapper mapper;
    @Autowired
    SysOssService sysOssService;

    @Override
    public List<Productcategory> getListByFather() {
        LambdaQueryWrapper<Productcategory> lqw = Wrappers.lambdaQuery();
        lqw.select(Productcategory::getCategoryId,Productcategory::getCategoryName,Productcategory::getCategoryIcon);
        lqw.eq(Productcategory::getParentId,0L);
        lqw.orderByAsc(Productcategory::getOrderNum);
        List<Productcategory> list = mapper.selectList(lqw);
        if (list.size() > 0) {
            for (Productcategory category : list) {
                String ossId = category.getCategoryIcon();
                if (StringUtils.isNotBlank(ossId)) {
                    SysOss oss = sysOssService.getById(Long.valueOf(ossId));
                    category.setCategoryIcon(oss.getUrl());
                }
            }
            return list;
        }
        return null;
    }

    @Override
    public List<Productcategory> getListByChild(Long parentId) {
        LambdaQueryWrapper<Productcategory> lqw = Wrappers.lambdaQuery();
        lqw.select(Productcategory::getCategoryId,Productcategory::getCategoryName);
        lqw.eq(Productcategory::getParentId,parentId);
        lqw.orderByAsc(Productcategory::getOrderNum);
        return mapper.selectList(lqw);
    }
}
