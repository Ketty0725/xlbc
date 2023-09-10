package com.ketty.serviceimpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ketty.api_entity.Chineseherbcategory;
import com.ketty.api_entity.Chineseherbcategoryintroduce;
import com.ketty.api_mapper.ChineseherbcategoryintroduceMapper;
import com.ketty.service.ChineseherbcategoryService;
import com.ketty.service.ChineseherbcategoryintroduceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ketty
 * @since 2023-04-04
 */
@Service
public class ChineseherbcategoryintroduceServiceImpl extends ServiceImpl<ChineseherbcategoryintroduceMapper, Chineseherbcategoryintroduce> implements ChineseherbcategoryintroduceService {
    @Autowired
    ChineseherbcategoryintroduceMapper mapper;
    @Autowired
    ChineseherbcategoryService categoryService;

    @Override
    public Chineseherbcategoryintroduce getByTitle(String title) {
        Chineseherbcategory category = categoryService.queryByName(title);
        if (category != null) {
            LambdaQueryWrapper<Chineseherbcategoryintroduce> lqw = Wrappers.lambdaQuery();
            lqw.eq(Chineseherbcategoryintroduce::getCategoryId,category.getCategoryId());
            return mapper.selectOne(lqw);
        }
        return null;
    }
}
