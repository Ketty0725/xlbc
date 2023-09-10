package com.xlbc.app.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xlbc.app.domain.Chineseherb;
import com.xlbc.app.domain.Chineseherbimage;
import com.xlbc.app.domain.Productimage;
import com.xlbc.app.service.IChineseherbimageService;
import com.xlbc.app.service.IProductimageService;
import com.xlbc.common.core.page.TableDataInfo;
import com.xlbc.common.core.domain.PageQuery;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xlbc.common.utils.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.xlbc.app.domain.Productbasicinfo;
import com.xlbc.app.mapper.ProductbasicinfoMapper;
import com.xlbc.app.service.IProductbasicinfoService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 药品说明Service业务层处理
 *
 * @author ketty
 * @date 2023-03-31
 */
@RequiredArgsConstructor
@Service
@DS("slave")
public class ProductbasicinfoServiceImpl implements IProductbasicinfoService {

    private final ProductbasicinfoMapper baseMapper;

    private final IProductimageService imageService;

    /**
     * 查询药品说明
     */
    @Override
    public Productbasicinfo queryById(Long id){
        Productbasicinfo productbasicinfo = baseMapper.selectVoById(id);
        Productimage productimage = imageService.queryByProductId(productbasicinfo.getProductId());
        productbasicinfo.setImages(productimage);
        return productbasicinfo;
    }

    /**
     * 查询药品说明列表
     */
    @Override
    public TableDataInfo<Productbasicinfo> queryPageList(Productbasicinfo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<Productbasicinfo> lqw = buildQueryWrapper(bo);
        Page<Productbasicinfo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        for (int i = 0; i < result.getRecords().size(); i++) {
            Productimage productimage = imageService.queryByProductId(
                result.getRecords().get(i).getProductId());
            result.getRecords().get(i).setImages(productimage);
        }
        return TableDataInfo.build(result);
    }

    /**
     * 查询药品说明列表
     */
    @Override
    public List<Productbasicinfo> queryList(Productbasicinfo bo) {
        LambdaQueryWrapper<Productbasicinfo> lqw = buildQueryWrapper(bo);
        List<Productbasicinfo> list = baseMapper.selectVoList(lqw);
        for (int i = 0; i < list.size(); i++) {
            Productimage productimage = imageService.queryByProductId(list.get(i).getProductId());
            list.get(i).setImages(productimage);
        }
        return list;
    }

    private LambdaQueryWrapper<Productbasicinfo> buildQueryWrapper(Productbasicinfo bo) {
        LambdaQueryWrapper<Productbasicinfo> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getProductId() != null, Productbasicinfo::getProductId, bo.getProductId());
        lqw.like(StringUtils.isNotBlank(bo.getCommonName()), Productbasicinfo::getCommonName, bo.getCommonName());
        lqw.like(StringUtils.isNotBlank(bo.getBrand()), Productbasicinfo::getBrand, bo.getBrand());
        lqw.like(StringUtils.isNotBlank(bo.getCompany()), Productbasicinfo::getCompany, bo.getCompany());
        return lqw;
    }

    /**
     * 新增药品说明
     */
    @Override
    public Boolean insert(Productbasicinfo bo) {
        Productbasicinfo add = BeanUtil.toBean(bo, Productbasicinfo.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        Productimage productimage = add.getImages();
        productimage.setProductId(add.getProductId());
        imageService.insert(productimage);
        return flag;
    }

    /**
     * 修改药品说明
     */
    @Override
    public Boolean update(Productbasicinfo bo) {
        Productbasicinfo update = BeanUtil.toBean(bo, Productbasicinfo.class);
        validEntityBeforeSave(update);
        imageService.update(update.getImages());
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(Productbasicinfo entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除药品说明
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            imageService.deleteByByProductIds(ids);
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
