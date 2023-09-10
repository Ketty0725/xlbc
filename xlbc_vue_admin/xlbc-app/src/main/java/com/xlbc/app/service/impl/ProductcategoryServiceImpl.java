package com.xlbc.app.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xlbc.app.domain.Chineseherbcategory;
import com.xlbc.common.utils.StringUtils;
import com.xlbc.system.domain.vo.SysOssVo;
import com.xlbc.system.service.ISysOssService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.xlbc.app.domain.Productcategory;
import com.xlbc.app.mapper.ProductcategoryMapper;
import com.xlbc.app.service.IProductcategoryService;

import java.util.ArrayList;
import java.util.List;
import java.util.Collection;

/**
 * 药品分类Service业务层处理
 *
 * @author ketty
 * @date 2023-03-26
 */
@RequiredArgsConstructor
@Service
@DS("slave")
public class ProductcategoryServiceImpl implements IProductcategoryService {

    private final ProductcategoryMapper baseMapper;

    private final ISysOssService ossService;

    /**
     * 查询药品分类
     */
    @Override
    public Productcategory queryById(Long categoryId){
        Productcategory productcategory = baseMapper.selectVoById(categoryId);
        String image = productcategory.getCategoryIcon();
        if (image != null) {
            SysOssVo oss = ossService.getById(Long.valueOf(image));
            productcategory.setImage(oss.getUrl());
        }
        return productcategory;
    }

    @Override
    public List<Productcategory> queryListByParent(Long parentId) {
        LambdaQueryWrapper<Productcategory> lqw = new LambdaQueryWrapper<>();
        lqw.select(Productcategory::getCategoryId,Productcategory::getCategoryName);
        lqw.eq(Productcategory::getParentId,parentId);
        return baseMapper.selectList(lqw);
    }

    /**
     * 查询药品分类列表
     */
    @Override
    public List<Productcategory> queryList(Productcategory bo) {
        LambdaQueryWrapper<Productcategory> lqw = buildQueryWrapper(bo);
        List<Productcategory> list = baseMapper.selectVoList(lqw);
        if (list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                String image = list.get(i).getCategoryIcon();
                if (image != null) {
                    SysOssVo oss = ossService.getById(Long.valueOf(image));
                    list.get(i).setImage(oss.getUrl());
                }
            }
        }
        return list;
    }

    private LambdaQueryWrapper<Productcategory> buildQueryWrapper(Productcategory bo) {
        LambdaQueryWrapper<Productcategory> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.isNotBlank(bo.getCategoryName()), Productcategory::getCategoryName, bo.getCategoryName());
        lqw.orderByAsc(Productcategory::getOrderNum);
        return lqw;
    }

    /**
     * 新增药品分类
     */
    @Override
    public Boolean insert(Productcategory bo) {
        Productcategory add = BeanUtil.toBean(bo, Productcategory.class);
        validEntityBeforeSave(add);
        return baseMapper.insert(add) > 0;
    }

    /**
     * 修改药品分类
     */
    @Override
    public Boolean update(Productcategory bo) {
        Productcategory update = BeanUtil.toBean(bo, Productcategory.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(Productcategory entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除药品分类
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if (isValid) {
            Collection<Long> ossIds = new ArrayList<>();
            for (Long id : ids) {
                Productcategory delete = baseMapper.selectOne(
                    new LambdaUpdateWrapper<Productcategory>()
                    .eq(Productcategory::getCategoryId,id));
                String ossId = delete.getCategoryIcon();
                if (ossId != null) {
                    ossIds.add(Long.valueOf(ossId));
                }
                baseMapper.delete(new LambdaUpdateWrapper<Productcategory>()
                    .eq(Productcategory::getParentId,id));
            }
            ossService.deleteWithValidByIds(ossIds, true);
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
