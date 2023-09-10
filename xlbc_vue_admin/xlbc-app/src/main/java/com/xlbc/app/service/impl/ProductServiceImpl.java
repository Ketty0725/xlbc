package com.xlbc.app.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.xlbc.app.domain.Chineseherbimage;
import com.xlbc.app.domain.User;
import com.xlbc.common.core.page.TableDataInfo;
import com.xlbc.common.core.domain.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xlbc.common.utils.StringUtils;
import com.xlbc.system.domain.vo.SysOssVo;
import com.xlbc.system.service.ISysOssService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.xlbc.app.domain.Product;
import com.xlbc.app.mapper.ProductMapper;
import com.xlbc.app.service.IProductService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 药品信息Service业务层处理
 *
 * @author ketty
 * @date 2023-03-26
 */
@RequiredArgsConstructor
@Service
@DS("slave")
public class ProductServiceImpl implements IProductService {

    private final ProductMapper baseMapper;

    private final ISysOssService ossService;

    /**
     * 查询药品信息
     */
    @Override
    public Product queryById(Long id){
        Product product = baseMapper.selectVoById(id);
        String image = product.getImage();
        SysOssVo oss = new SysOssVo();
        if (image != null && !"".equals(image)) {
            oss = ossService.getById(Long.valueOf(image));
        }
        product.setOss(oss);
        return product;
    }

    @Override
    public List<Product> queryByName(String name) {
        return baseMapper.selectList(new LambdaQueryWrapper<Product>().like(Product::getName,name));
    }

    @Override
    public List<Product> queryListAll() {
        return baseMapper.selectList(
            new LambdaQueryWrapper<Product>()
                .select(Product::getId,Product::getName));
    }

    /**
     * 查询药品信息列表
     */
    @Override
    public TableDataInfo<Product> queryPageList(Product bo, PageQuery pageQuery) {
        LambdaQueryWrapper<Product> lqw = buildQueryWrapper(bo);
        Page<Product> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        for (int i = 0; i < result.getRecords().size(); i++) {
            String image = result.getRecords().get(i).getImage();
            SysOssVo oss = new SysOssVo();
            if (image != null && !"".equals(image)) {
                oss = ossService.getById(Long.valueOf(image));
            }
            result.getRecords().get(i).setOss(oss);
        }
        return TableDataInfo.build(result);
    }

    /**
     * 查询药品信息列表
     */
    @Override
    public List<Product> queryList(Product bo) {
        LambdaQueryWrapper<Product> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<Product> buildQueryWrapper(Product bo) {
        LambdaQueryWrapper<Product> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.isNotBlank(bo.getName()), Product::getName, bo.getName());
        lqw.eq(bo.getCategoryId() != null, Product::getCategoryId, bo.getCategoryId());
        return lqw;
    }

    /**
     * 新增药品信息
     */
    @Override
    public Boolean insert(Product bo) {
        Product add = BeanUtil.toBean(bo, Product.class);
        validEntityBeforeSave(add);
        return baseMapper.insert(add) > 0;
    }

    /**
     * 修改药品信息
     */
    @Override
    public Boolean update(Product bo) {
        Product update = BeanUtil.toBean(bo, Product.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(Product entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除药品信息
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
