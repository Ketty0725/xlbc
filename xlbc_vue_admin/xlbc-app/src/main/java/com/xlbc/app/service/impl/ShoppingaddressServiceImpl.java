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
import com.xlbc.app.domain.Shoppingaddress;
import com.xlbc.app.mapper.ShoppingaddressMapper;
import com.xlbc.app.service.IShoppingaddressService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 收货地址Service业务层处理
 *
 * @author ketty
 * @date 2023-04-08
 */
@RequiredArgsConstructor
@Service
@DS("slave")
public class ShoppingaddressServiceImpl implements IShoppingaddressService {

    private final ShoppingaddressMapper baseMapper;

    /**
     * 查询收货地址
     */
    @Override
    public Shoppingaddress queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    @Override
    public Shoppingaddress queryByUserId(Long userId) {
        LambdaQueryWrapper<Shoppingaddress> lqw = Wrappers.lambdaQuery();
        lqw.eq(userId != null, Shoppingaddress::getUserId, userId);
        return baseMapper.selectVoOne(lqw);
    }

    /**
     * 查询收货地址列表
     */
    @Override
    public TableDataInfo<Shoppingaddress> queryPageList(Shoppingaddress bo, PageQuery pageQuery) {
        LambdaQueryWrapper<Shoppingaddress> lqw = buildQueryWrapper(bo);
        Page<Shoppingaddress> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询收货地址列表
     */
    @Override
    public List<Shoppingaddress> queryList(Shoppingaddress bo) {
        LambdaQueryWrapper<Shoppingaddress> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<Shoppingaddress> buildQueryWrapper(Shoppingaddress bo) {
        LambdaQueryWrapper<Shoppingaddress> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getUserId() != null, Shoppingaddress::getUserId, bo.getUserId());
        lqw.like(StringUtils.isNotBlank(bo.getName()), Shoppingaddress::getName, bo.getName());
        lqw.eq(StringUtils.isNotBlank(bo.getPhone()), Shoppingaddress::getPhone, bo.getPhone());
        lqw.eq(StringUtils.isNotBlank(bo.getArea()), Shoppingaddress::getArea, bo.getArea());
        lqw.eq(StringUtils.isNotBlank(bo.getAddress()), Shoppingaddress::getAddress, bo.getAddress());
        lqw.eq(bo.getIsDefault() != null, Shoppingaddress::getIsDefault, bo.getIsDefault());
        return lqw;
    }

    /**
     * 新增收货地址
     */
    @Override
    public Boolean insert(Shoppingaddress bo) {
        Shoppingaddress add = BeanUtil.toBean(bo, Shoppingaddress.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改收货地址
     */
    @Override
    public Boolean update(Shoppingaddress bo) {
        Shoppingaddress update = BeanUtil.toBean(bo, Shoppingaddress.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(Shoppingaddress entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除收货地址
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
