package com.xlbc.app.service;

import com.xlbc.app.domain.Medicateddiet;
import com.xlbc.common.core.page.TableDataInfo;
import com.xlbc.common.core.domain.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 药膳数据Service接口
 *
 * @author ketty
 * @date 2023-04-02
 */
public interface IMedicateddietService {

    /**
     * 查询药膳数据
     */
    Medicateddiet queryById(Long id);

    /**
     * 查询药膳数据列表
     */
    TableDataInfo<Medicateddiet> queryPageList(Medicateddiet bo, PageQuery pageQuery);

    /**
     * 查询药膳数据列表
     */
    List<Medicateddiet> queryList(Medicateddiet bo);

    /**
     * 新增药膳数据
     */
    Boolean insert(Medicateddiet bo);

    /**
     * 修改药膳数据
     */
    Boolean update(Medicateddiet bo);

    /**
     * 校验并批量删除药膳数据信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
