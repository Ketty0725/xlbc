package com.xlbc.app.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.xlbc.app.domain.Chineseherb;
import com.xlbc.app.service.IChineseherbcategoryService;
import com.xlbc.app.service.IPrescriptioncategoryService;
import com.xlbc.common.core.page.TableDataInfo;
import com.xlbc.common.core.domain.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xlbc.common.utils.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.xlbc.app.domain.Prescription;
import com.xlbc.app.mapper.PrescriptionMapper;
import com.xlbc.app.service.IPrescriptionService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 方剂数据Service业务层处理
 *
 * @author ketty
 * @date 2023-04-01
 */
@RequiredArgsConstructor
@Service
@DS("slave")
public class PrescriptionServiceImpl implements IPrescriptionService {

    private final PrescriptionMapper baseMapper;

    private final IPrescriptioncategoryService categoryService;

    /**
     * 查询方剂数据
     */
    @Override
    public Prescription queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询方剂数据列表
     */
    @Override
    public TableDataInfo<Prescription> queryPageList(Prescription bo, PageQuery pageQuery) {
        LambdaQueryWrapper<Prescription> lqw = buildQueryWrapper(bo);
        Page<Prescription> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询方剂数据列表
     */
    @Override
    public List<Prescription> queryList(Prescription bo) {
        LambdaQueryWrapper<Prescription> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<Prescription> buildQueryWrapper(Prescription bo) {
        LambdaQueryWrapper<Prescription> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.isNotBlank(bo.getPrescriptionName()), Prescription::getPrescriptionName, bo.getPrescriptionName());
        lqw.like(StringUtils.isNotBlank(bo.getProvenance()), Prescription::getProvenance, bo.getProvenance());
        if (bo.getCategoryId() != null) {
            lqw.eq(Prescription::getCategoryId, bo.getCategoryId());
        }
        lqw.like(StringUtils.isNotBlank(bo.getEfficacy()), Prescription::getEfficacy, bo.getEfficacy());
        return lqw;
    }

    /**
     * 新增方剂数据
     */
    @Override
    public Boolean insert(Prescription bo) {
        Prescription add = BeanUtil.toBean(bo, Prescription.class);
        add.setCategory(selectCategoryName(add.getCategoryId()));
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        return flag;
    }

    /**
     * 修改方剂数据
     */
    @Override
    public Boolean update(Prescription bo) {
        Prescription update = BeanUtil.toBean(bo, Prescription.class);
        update.setCategory(selectCategoryName(update.getCategoryId()));
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(Prescription entity){
        //TODO 做一些数据校验,如唯一约束
    }

    public String selectCategoryName(Long categoryId) {
        return categoryService.selectCategoryName(categoryId);
    }

    /**
     * 批量删除方剂数据
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
