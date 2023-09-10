package com.xlbc.app.service;

import com.xlbc.app.domain.Shoppingaddress;
import com.xlbc.common.core.page.TableDataInfo;
import com.xlbc.common.core.domain.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 收货地址Service接口
 *
 * @author ketty
 * @date 2023-04-08
 */
public interface IShoppingaddressService {

    /**
     * 查询收货地址
     */
    Shoppingaddress queryById(Long id);

    Shoppingaddress queryByUserId(Long userId);

    /**
     * 查询收货地址列表
     */
    TableDataInfo<Shoppingaddress> queryPageList(Shoppingaddress bo, PageQuery pageQuery);

    /**
     * 查询收货地址列表
     */
    List<Shoppingaddress> queryList(Shoppingaddress bo);

    /**
     * 新增收货地址
     */
    Boolean insert(Shoppingaddress bo);

    /**
     * 修改收货地址
     */
    Boolean update(Shoppingaddress bo);

    /**
     * 校验并批量删除收货地址信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
