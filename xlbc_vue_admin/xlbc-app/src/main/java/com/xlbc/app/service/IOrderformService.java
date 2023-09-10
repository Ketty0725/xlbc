package com.xlbc.app.service;

import com.xlbc.app.domain.Orderform;
import com.xlbc.app.domain.Product;
import com.xlbc.app.domain.User;
import com.xlbc.common.core.page.TableDataInfo;
import com.xlbc.common.core.domain.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 订单管理Service接口
 *
 * @author ketty
 * @date 2023-04-05
 */
public interface IOrderformService {

    /**
     * 查询订单管理
     */
    Orderform queryById(Long id);

    /**
     * 查询订单管理列表
     */
    TableDataInfo<Orderform> queryPageList(Orderform bo, String uName, String pName, PageQuery pageQuery);

    /**
     * 查询订单管理列表
     */
    List<Orderform> queryList(Orderform bo);

    /**
     * 新增订单管理
     */
    Boolean insert(Orderform bo);

    /**
     * 修改订单管理
     */
    Boolean update(Long id);

    /**
     * 校验并批量删除订单管理信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
