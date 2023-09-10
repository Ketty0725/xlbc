package com.xlbc.app.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xlbc.common.utils.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.xlbc.app.domain.Chineseherbcategory;
import com.xlbc.app.mapper.ChineseherbcategoryMapper;
import com.xlbc.app.service.IChineseherbcategoryService;

import java.util.List;
import java.util.Collection;

/**
 * 中药分类Service业务层处理
 *
 * @author ketty
 * @date 2023-03-19
 */
@RequiredArgsConstructor
@Service
@DS("slave")
public class ChineseherbcategoryServiceImpl implements IChineseherbcategoryService {

    private final ChineseherbcategoryMapper baseMapper;

    /**
     * 查询中药分类
     */
    @Override
    public Chineseherbcategory queryById(Long categoryId){
        return baseMapper.selectById(categoryId);
    }

    @Override
    public List<Chineseherbcategory> queryListByParent(Long parentId) {
        LambdaQueryWrapper<Chineseherbcategory> lqw = Wrappers.lambdaQuery();
        lqw.select(Chineseherbcategory::getCategoryId,Chineseherbcategory::getCategoryName);
        lqw.eq(Chineseherbcategory::getParentId,parentId);
        return baseMapper.selectList(lqw);
    }

    /**
     * 查询中药分类列表
     */
    @Override
    public List<Chineseherbcategory> queryList(Chineseherbcategory bean) {
        LambdaQueryWrapper<Chineseherbcategory> lqw = buildQueryWrapper(bean);
        return baseMapper.selectList(lqw);
    }

    private LambdaQueryWrapper<Chineseherbcategory> buildQueryWrapper(Chineseherbcategory bean) {
        LambdaQueryWrapper<Chineseherbcategory> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.isNotBlank(bean.getCategoryName()), Chineseherbcategory::getCategoryName, bean.getCategoryName());
        return lqw;
    }

    /**
     * 新增中药分类
     */
    @Override
    public Boolean insert(Chineseherbcategory bean) {
        Chineseherbcategory add = BeanUtil.toBean(bean, Chineseherbcategory.class);
        validEntityBeforeSave(add);
        return baseMapper.insert(add) > 0;
    }

    /**
     * 修改中药分类
     */
    @Override
    public Boolean update(Chineseherbcategory bean) {
        Chineseherbcategory update = BeanUtil.toBean(bean, Chineseherbcategory.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(Chineseherbcategory entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除中药分类
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if (isValid) {
            for (Long id: ids) {
                baseMapper.delete(new LambdaUpdateWrapper<Chineseherbcategory>()
                    .eq(Chineseherbcategory::getParentId,id));
            }
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }

    @Override
    public String selectCategoryName(Long id) {
        Chineseherbcategory child = baseMapper.selectById(id);
        Chineseherbcategory father = baseMapper.selectById(child.getParentId());
        return father.getCategoryName() + "-" + child.getCategoryName();
    }
}
