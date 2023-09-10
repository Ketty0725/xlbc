package com.xlbc.app.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.xlbc.common.core.page.TableDataInfo;
import com.xlbc.common.core.domain.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xlbc.common.utils.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.xlbc.app.domain.Medicateddiet;
import com.xlbc.app.mapper.MedicateddietMapper;
import com.xlbc.app.service.IMedicateddietService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 药膳数据Service业务层处理
 *
 * @author ketty
 * @date 2023-04-02
 */
@RequiredArgsConstructor
@Service
@DS("slave")
public class MedicateddietServiceImpl implements IMedicateddietService {

    private final MedicateddietMapper baseMapper;

    /**
     * 查询药膳数据
     */
    @Override
    public Medicateddiet queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询药膳数据列表
     */
    @Override
    public TableDataInfo<Medicateddiet> queryPageList(Medicateddiet bo, PageQuery pageQuery) {
        LambdaQueryWrapper<Medicateddiet> lqw = buildQueryWrapper(bo);
        Page<Medicateddiet> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询药膳数据列表
     */
    @Override
    public List<Medicateddiet> queryList(Medicateddiet bo) {
        LambdaQueryWrapper<Medicateddiet> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<Medicateddiet> buildQueryWrapper(Medicateddiet bo) {
        LambdaQueryWrapper<Medicateddiet> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.isNotBlank(bo.getMedicatedDietName()), Medicateddiet::getMedicatedDietName, bo.getMedicatedDietName());
        lqw.like(StringUtils.isNotBlank(bo.getFoodMaterial()), Medicateddiet::getFoodMaterial, bo.getFoodMaterial());
        lqw.like(StringUtils.isNotBlank(bo.getEfficacy()), Medicateddiet::getEfficacy, bo.getEfficacy());
        return lqw;
    }

    /**
     * 新增药膳数据
     */
    @Override
    public Boolean insert(Medicateddiet bo) {
        Medicateddiet add = BeanUtil.toBean(bo, Medicateddiet.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改药膳数据
     */
    @Override
    public Boolean update(Medicateddiet bo) {
        Medicateddiet update = BeanUtil.toBean(bo, Medicateddiet.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(Medicateddiet entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除药膳数据
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
