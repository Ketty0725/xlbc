package com.ketty.serviceimpl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ketty.api_entity.Chineseherbcategory;
import com.ketty.api_mapper.ChineseherbcategoryMapper;
import com.ketty.service.ChineseherbcategoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ketty
 * @since 2023-04-04
 */
@Service
@DS("master")
public class ChineseherbcategoryServiceImpl extends ServiceImpl<ChineseherbcategoryMapper, Chineseherbcategory> implements ChineseherbcategoryService {
    @Autowired
    ChineseherbcategoryMapper baseMapper;

    @Override
    public List<Chineseherbcategory> queryListByParent(Long parentId) {
        LambdaQueryWrapper<Chineseherbcategory> lqw = Wrappers.lambdaQuery();
        lqw.select(Chineseherbcategory::getCategoryId,Chineseherbcategory::getCategoryName);
        lqw.eq(Chineseherbcategory::getParentId,parentId);
        return baseMapper.selectList(lqw);
    }

    @Override
    public Chineseherbcategory queryByName(String categoryName) {
        LambdaQueryWrapper<Chineseherbcategory> lqw = Wrappers.lambdaQuery();
        lqw.select(Chineseherbcategory::getCategoryId);
        lqw.eq(Chineseherbcategory::getCategoryName,categoryName);
        return baseMapper.selectOne(lqw);
    }
}
