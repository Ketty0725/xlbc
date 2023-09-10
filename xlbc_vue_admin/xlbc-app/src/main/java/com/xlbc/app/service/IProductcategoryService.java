package com.xlbc.app.service;

import com.xlbc.app.domain.Productcategory;

import java.util.Collection;
import java.util.List;

/**
 * 药品分类Service接口
 *
 * @author ketty
 * @date 2023-03-26
 */
public interface IProductcategoryService {

    /**
     * 查询药品分类
     */
    Productcategory queryById(Long categoryId);

    List<Productcategory> queryListByParent(Long parentId);

    /**
     * 查询药品分类列表
     */
    List<Productcategory> queryList(Productcategory bo);

    /**
     * 新增药品分类
     */
    Boolean insert(Productcategory bo);

    /**
     * 修改药品分类
     */
    Boolean update(Productcategory bo);

    /**
     * 校验并批量删除药品分类信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

}
