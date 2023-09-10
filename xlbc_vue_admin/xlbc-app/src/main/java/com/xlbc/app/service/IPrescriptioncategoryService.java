package com.xlbc.app.service;

import com.xlbc.app.domain.Chineseherbcategory;
import com.xlbc.app.domain.Prescriptioncategory;

import java.util.Collection;
import java.util.List;

/**
 * 方剂分类Service接口
 *
 * @author ketty
 * @date 2023-04-01
 */
public interface IPrescriptioncategoryService {

    /**
     * 查询方剂分类
     */
    Prescriptioncategory queryById(Long categoryId);


    List<Prescriptioncategory> queryListByParent(Long parentId);

    /**
     * 查询方剂分类列表
     */
    List<Prescriptioncategory> queryList(Prescriptioncategory bo);

    /**
     * 新增方剂分类
     */
    Boolean insert(Prescriptioncategory bo);

    /**
     * 修改方剂分类
     */
    Boolean update(Prescriptioncategory bo);

    /**
     * 校验并批量删除方剂分类信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    String selectCategoryName(Long id);
}
