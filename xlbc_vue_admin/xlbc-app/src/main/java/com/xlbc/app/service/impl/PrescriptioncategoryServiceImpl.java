package com.xlbc.app.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xlbc.app.domain.Chineseherbcategory;
import com.xlbc.common.utils.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.xlbc.app.domain.Prescriptioncategory;
import com.xlbc.app.mapper.PrescriptioncategoryMapper;
import com.xlbc.app.service.IPrescriptioncategoryService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 方剂分类Service业务层处理
 *
 * @author ketty
 * @date 2023-04-01
 */
@RequiredArgsConstructor
@Service
@DS("slave")
public class PrescriptioncategoryServiceImpl implements IPrescriptioncategoryService {

    private final PrescriptioncategoryMapper baseMapper;

    /**
     * 查询方剂分类
     */
    @Override
    public Prescriptioncategory queryById(Long categoryId){
        return baseMapper.selectVoById(categoryId);
    }

    @Override
    public List<Prescriptioncategory> queryListByParent(Long parentId) {
        LambdaQueryWrapper<Prescriptioncategory> lqw = new LambdaQueryWrapper<>();
        lqw.select(Prescriptioncategory::getCategoryId,Prescriptioncategory::getCategoryName);
        lqw.eq(Prescriptioncategory::getParentId,parentId);
        return baseMapper.selectList(lqw);
    }

    /**
     * 查询方剂分类列表
     */
    @Override
    public List<Prescriptioncategory> queryList(Prescriptioncategory bo) {
        LambdaQueryWrapper<Prescriptioncategory> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<Prescriptioncategory> buildQueryWrapper(Prescriptioncategory bo) {
        LambdaQueryWrapper<Prescriptioncategory> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.isNotBlank(bo.getCategoryName()), Prescriptioncategory::getCategoryName, bo.getCategoryName());
        return lqw;
    }

    /**
     * 新增方剂分类
     */
    @Override
    public Boolean insert(Prescriptioncategory bo) {
        Prescriptioncategory add = BeanUtil.toBean(bo, Prescriptioncategory.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        return flag;
    }

    /**
     * 修改方剂分类
     */
    @Override
    public Boolean update(Prescriptioncategory bo) {
        Prescriptioncategory update = BeanUtil.toBean(bo, Prescriptioncategory.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(Prescriptioncategory entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除方剂分类
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            for (Long id: ids) {
                baseMapper.delete(new LambdaUpdateWrapper<Prescriptioncategory>()
                    .eq(Prescriptioncategory::getParentId,id));
            }
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }

    @Override
    public String selectCategoryName(Long id) {
        Prescriptioncategory child = baseMapper.selectById(id);
        Prescriptioncategory father = baseMapper.selectById(child.getParentId());
        return father.getCategoryName() + "-" + child.getCategoryName();
    }
}
