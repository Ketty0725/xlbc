package com.xlbc.app.service;

import com.xlbc.app.domain.Chineseherbcategory;

import java.util.Collection;
import java.util.List;

/**
 * 中药分类Service接口
 *
 * @author ketty
 * @date 2023-03-19
 */
public interface IChineseherbcategoryService {

    /**
     * 查询中药分类
     */
    Chineseherbcategory queryById(Long categoryId);

    List<Chineseherbcategory> queryListByParent(Long parentId);

    /**
     * 查询中药分类列表
     */
    List<Chineseherbcategory> queryList(Chineseherbcategory bean);

    /**
     * 新增中药分类
     */
    Boolean insert(Chineseherbcategory bean);

    /**
     * 修改中药分类
     */
    Boolean update(Chineseherbcategory bean);

    /**
     * 校验并批量删除中药分类信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    String selectCategoryName(Long id);

}
