package com.xlbc.app.service;

import com.xlbc.app.domain.Chineseherbcategoryintroduce;
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
public interface IChineseherbcategoryintroduceService {

    /**
     * 查询分类介绍
     */
    Chineseherbcategoryintroduce queryById(Long id);

    /**
     * 查询分类介绍列表
     */
    TableDataInfo<Chineseherbcategoryintroduce> queryPageList(Chineseherbcategoryintroduce bo, PageQuery pageQuery);

    /**
     * 查询分类介绍列表
     */
    List<Chineseherbcategoryintroduce> queryList(Chineseherbcategoryintroduce bo);

    /**
     * 新增分类介绍
     */
    Boolean insert(Chineseherbcategoryintroduce bo);

    /**
     * 修改分类介绍
     */
    Boolean update(Chineseherbcategoryintroduce bo);

    /**
     * 校验并批量删除分类介绍信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
