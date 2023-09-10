package com.xlbc.app.service;

import com.xlbc.app.domain.Prescription;
import com.xlbc.common.core.page.TableDataInfo;
import com.xlbc.common.core.domain.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 方剂数据Service接口
 *
 * @author ketty
 * @date 2023-04-01
 */
public interface IPrescriptionService {

    /**
     * 查询方剂数据
     */
    Prescription queryById(Long id);

    /**
     * 查询方剂数据列表
     */
    TableDataInfo<Prescription> queryPageList(Prescription bo, PageQuery pageQuery);

    /**
     * 查询方剂数据列表
     */
    List<Prescription> queryList(Prescription bo);

    /**
     * 新增方剂数据
     */
    Boolean insert(Prescription bo);

    /**
     * 修改方剂数据
     */
    Boolean update(Prescription bo);

    /**
     * 校验并批量删除方剂数据信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
