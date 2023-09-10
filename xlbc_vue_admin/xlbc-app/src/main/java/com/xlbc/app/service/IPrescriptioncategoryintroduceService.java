package com.xlbc.app.service;

import com.xlbc.app.domain.Prescriptioncategoryintroduce;
import com.xlbc.common.core.page.TableDataInfo;
import com.xlbc.common.core.domain.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 分类介绍Service接口
 *
 * @author ketty
 * @date 2023-04-01
 */
public interface IPrescriptioncategoryintroduceService {

    /**
     * 查询分类介绍
     */
    Prescriptioncategoryintroduce queryById(Long id);

    /**
     * 查询分类介绍列表
     */
    TableDataInfo<Prescriptioncategoryintroduce> queryPageList(Prescriptioncategoryintroduce bo, PageQuery pageQuery);

    /**
     * 查询分类介绍列表
     */
    List<Prescriptioncategoryintroduce> queryList(Prescriptioncategoryintroduce bo);

    /**
     * 新增分类介绍
     */
    Boolean insert(Prescriptioncategoryintroduce bo);

    /**
     * 修改分类介绍
     */
    Boolean update(Prescriptioncategoryintroduce bo);

    /**
     * 校验并批量删除分类介绍信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
