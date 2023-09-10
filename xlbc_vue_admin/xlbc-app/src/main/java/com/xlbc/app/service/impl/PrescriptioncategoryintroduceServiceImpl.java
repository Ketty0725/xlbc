package com.xlbc.app.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.xlbc.app.domain.Chineseherbcategoryintroduce;
import com.xlbc.common.core.page.TableDataInfo;
import com.xlbc.common.core.domain.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.xlbc.app.domain.Prescriptioncategoryintroduce;
import com.xlbc.app.mapper.PrescriptioncategoryintroduceMapper;
import com.xlbc.app.service.IPrescriptioncategoryintroduceService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 分类介绍Service业务层处理
 *
 * @author ketty
 * @date 2023-04-01
 */
@RequiredArgsConstructor
@Service
@DS("slave")
public class PrescriptioncategoryintroduceServiceImpl implements IPrescriptioncategoryintroduceService {

    private final PrescriptioncategoryintroduceMapper baseMapper;

    /**
     * 查询分类介绍
     */
    @Override
    public Prescriptioncategoryintroduce queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询分类介绍列表
     */
    @Override
    public TableDataInfo<Prescriptioncategoryintroduce> queryPageList(Prescriptioncategoryintroduce bo, PageQuery pageQuery) {
        LambdaQueryWrapper<Prescriptioncategoryintroduce> lqw = buildQueryWrapper(bo);
        Page<Prescriptioncategoryintroduce> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询分类介绍列表
     */
    @Override
    public List<Prescriptioncategoryintroduce> queryList(Prescriptioncategoryintroduce bo) {
        LambdaQueryWrapper<Prescriptioncategoryintroduce> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<Prescriptioncategoryintroduce> buildQueryWrapper(Prescriptioncategoryintroduce bo) {
        LambdaQueryWrapper<Prescriptioncategoryintroduce> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getCategoryId() != null, Prescriptioncategoryintroduce::getCategoryId, bo.getCategoryId());
        return lqw;
    }

    /**
     * 新增分类介绍
     */
    @Override
    public Boolean insert(Prescriptioncategoryintroduce bo) {
        Prescriptioncategoryintroduce add = BeanUtil.toBean(bo, Prescriptioncategoryintroduce.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改分类介绍
     */
    @Override
    public Boolean update(Prescriptioncategoryintroduce bo) {
        Prescriptioncategoryintroduce update = BeanUtil.toBean(bo, Prescriptioncategoryintroduce.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(Prescriptioncategoryintroduce entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除分类介绍
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
