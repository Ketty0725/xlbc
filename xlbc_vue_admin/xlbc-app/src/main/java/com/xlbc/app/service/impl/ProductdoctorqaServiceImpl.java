package com.xlbc.app.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.xlbc.app.domain.Productbasicinfo;
import com.xlbc.common.core.page.TableDataInfo;
import com.xlbc.common.core.domain.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xlbc.common.utils.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.xlbc.app.domain.Productdoctorqa;
import com.xlbc.app.mapper.ProductdoctorqaMapper;
import com.xlbc.app.service.IProductdoctorqaService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 医生问答Service业务层处理
 *
 * @author ketty
 * @date 2023-04-01
 */
@RequiredArgsConstructor
@Service
@DS("slave")
public class ProductdoctorqaServiceImpl implements IProductdoctorqaService {

    private final ProductdoctorqaMapper baseMapper;

    /**
     * 查询医生问答
     */
    @Override
    public Productdoctorqa queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询医生问答列表
     */
    @Override
    public TableDataInfo<Productdoctorqa> queryPageList(Productdoctorqa bo, PageQuery pageQuery) {
        LambdaQueryWrapper<Productdoctorqa> lqw = buildQueryWrapper(bo);
        Page<Productdoctorqa> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询医生问答列表
     */
    @Override
    public List<Productdoctorqa> queryList(Productdoctorqa bo) {
        LambdaQueryWrapper<Productdoctorqa> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<Productdoctorqa> buildQueryWrapper(Productdoctorqa bo) {
        LambdaQueryWrapper<Productdoctorqa> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getProductId() != null, Productdoctorqa::getProductId, bo.getProductId());
        lqw.like(StringUtils.isNotBlank(bo.getAsk()), Productdoctorqa::getAsk, bo.getAsk());
        return lqw;
    }

    /**
     * 新增医生问答
     */
    @Override
    public Boolean insert(Productdoctorqa bo) {
        Productdoctorqa add = BeanUtil.toBean(bo, Productdoctorqa.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改医生问答
     */
    @Override
    public Boolean update(Productdoctorqa bo) {
        Productdoctorqa update = BeanUtil.toBean(bo, Productdoctorqa.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(Productdoctorqa entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除医生问答
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
