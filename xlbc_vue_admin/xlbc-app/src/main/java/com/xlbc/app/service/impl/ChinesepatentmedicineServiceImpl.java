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
import com.xlbc.app.domain.Chinesepatentmedicine;
import com.xlbc.app.mapper.ChinesepatentmedicineMapper;
import com.xlbc.app.service.IChinesepatentmedicineService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 中成药数据Service业务层处理
 *
 * @author ketty
 * @date 2023-04-02
 */
@RequiredArgsConstructor
@Service
@DS("slave")
public class ChinesepatentmedicineServiceImpl implements IChinesepatentmedicineService {

    private final ChinesepatentmedicineMapper baseMapper;

    /**
     * 查询中成药数据
     */
    @Override
    public Chinesepatentmedicine queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询中成药数据列表
     */
    @Override
    public TableDataInfo<Chinesepatentmedicine> queryPageList(Chinesepatentmedicine bo, PageQuery pageQuery) {
        LambdaQueryWrapper<Chinesepatentmedicine> lqw = buildQueryWrapper(bo);
        Page<Chinesepatentmedicine> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询中成药数据列表
     */
    @Override
    public List<Chinesepatentmedicine> queryList(Chinesepatentmedicine bo) {
        LambdaQueryWrapper<Chinesepatentmedicine> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<Chinesepatentmedicine> buildQueryWrapper(Chinesepatentmedicine bo) {
        LambdaQueryWrapper<Chinesepatentmedicine> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.isNotBlank(bo.getDrugName()), Chinesepatentmedicine::getDrugName, bo.getDrugName());
        lqw.like(StringUtils.isNotBlank(bo.getPrescription()), Chinesepatentmedicine::getPrescription, bo.getPrescription());
        lqw.like(StringUtils.isNotBlank(bo.getAttending()), Chinesepatentmedicine::getAttending, bo.getAttending());
        return lqw;
    }

    /**
     * 新增中成药数据
     */
    @Override
    public Boolean insert(Chinesepatentmedicine bo) {
        Chinesepatentmedicine add = BeanUtil.toBean(bo, Chinesepatentmedicine.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改中成药数据
     */
    @Override
    public Boolean update(Chinesepatentmedicine bo) {
        Chinesepatentmedicine update = BeanUtil.toBean(bo, Chinesepatentmedicine.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(Chinesepatentmedicine entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除中成药数据
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
