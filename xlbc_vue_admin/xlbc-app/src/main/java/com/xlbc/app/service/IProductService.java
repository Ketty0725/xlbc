package com.xlbc.app.service;

import com.xlbc.app.domain.Product;
import com.xlbc.app.domain.User;
import com.xlbc.common.core.domain.R;
import com.xlbc.common.core.page.TableDataInfo;
import com.xlbc.common.core.domain.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 药品信息Service接口
 *
 * @author ketty
 * @date 2023-03-26
 */
public interface IProductService {

    /**
     * 查询药品信息
     */
    Product queryById(Long id);

    List<Product> queryByName(String name);

    List<Product> queryListAll();

    /**
     * 查询药品信息列表
     */
    TableDataInfo<Product> queryPageList(Product bo, PageQuery pageQuery);

    /**
     * 查询药品信息列表
     */
    List<Product> queryList(Product bo);

    /**
     * 新增药品信息
     */
    Boolean insert(Product bo);

    /**
     * 修改药品信息
     */
    Boolean update(Product bo);

    /**
     * 校验并批量删除药品信息信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

}
