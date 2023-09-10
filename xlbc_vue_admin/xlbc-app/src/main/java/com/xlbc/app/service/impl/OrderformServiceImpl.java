package com.xlbc.app.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.xlbc.app.domain.Product;
import com.xlbc.app.domain.User;
import com.xlbc.app.service.IProductService;
import com.xlbc.app.service.IShoppingaddressService;
import com.xlbc.app.service.IUserService;
import com.xlbc.common.core.page.TableDataInfo;
import com.xlbc.common.core.domain.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.xlbc.app.domain.Orderform;
import com.xlbc.app.mapper.OrderformMapper;
import com.xlbc.app.service.IOrderformService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 订单管理Service业务层处理
 *
 * @author ketty
 * @date 2023-04-05
 */
@RequiredArgsConstructor
@Service
@DS("slave")
public class OrderformServiceImpl implements IOrderformService {

    private final OrderformMapper baseMapper;

    private final IProductService productService;

    private final IUserService userService;

    private final IShoppingaddressService addressService;

    /**
     * 查询订单管理
     */
    @Override
    public Orderform queryById(Long id){
        Orderform o = baseMapper.selectVoById(id);
        o.setProduct(productService.queryById(o.getProductId()));
        o.setUser(userService.queryById(o.getUserId()));
        o.setAddress(addressService.queryById(o.getAddressId()));
        return o;
    }

    /**
     * 查询订单管理列表
     */
    @Override
    public TableDataInfo<Orderform> queryPageList(Orderform bo, String uName, String pName, PageQuery pageQuery) {
        LambdaQueryWrapper<Orderform> lqw = buildQueryWrapper(bo,uName,pName);
        Page<Orderform> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        for (Orderform o: result.getRecords()) {
            o.setProduct(productService.queryById(o.getProductId()));
            o.setUser(userService.queryById(o.getUserId()));
            o.setAddress(addressService.queryById(o.getAddressId()));
        }
        return TableDataInfo.build(result);
    }

    /**
     * 查询订单管理列表
     */
    @Override
    public List<Orderform> queryList(Orderform bo) {
        LambdaQueryWrapper<Orderform> lqw = buildQueryWrapper(bo,null,null);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<Orderform> buildQueryWrapper(Orderform bo, String uName, String pName) {
        LambdaQueryWrapper<Orderform> lqw = Wrappers.lambdaQuery();
        if (uName != null) {
            List<User> users = userService.queryByName(uName);
            if (users.size() > 0) {
                for (User u : users) {
                    lqw.eq(u.getUId() != null, Orderform::getUserId, u.getUId());
                }
            }
        }
        if (pName != null) {
            List<Product> products = productService.queryByName(pName);
            if (products.size() > 0) {
                for (Product p : products) {
                    lqw.eq(p.getId() != null, Orderform::getProductId, p.getId());
                }
            }
        }
        lqw.eq(bo.getState() != null, Orderform::getState, bo.getState());
        return lqw;
    }

    /**
     * 新增订单管理
     */
    @Override
    public Boolean insert(Orderform bo) {
        Orderform add = BeanUtil.toBean(bo, Orderform.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    @Override
    public Boolean update(Long id) {
        Orderform o = baseMapper.selectVoById(id);
        int i = 0;
        if (o != null && o.getState() == 0) {
            i = baseMapper.update(null,new LambdaUpdateWrapper<Orderform>()
                .eq(Orderform::getId,id)
                .set(Orderform::getState,1));
            if (i > 0) {
                o = baseMapper.selectVoById(id);
                i = baseMapper.update(null,new LambdaUpdateWrapper<Orderform>()
                    .eq(Orderform::getId,id)
                    .set(Orderform::getShipmentTime,o.getUpdateTime()));
            }
        }
        return i > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(Orderform entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除订单管理
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
