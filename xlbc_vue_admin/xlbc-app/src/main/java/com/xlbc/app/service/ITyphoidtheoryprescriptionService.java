package com.xlbc.app.service;

import com.xlbc.app.domain.Typhoidtheoryprescription;
import com.xlbc.common.core.page.TableDataInfo;
import com.xlbc.common.core.domain.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 经方Service接口
 *
 * @author ketty
 * @date 2023-04-02
 */
public interface ITyphoidtheoryprescriptionService {

    /**
     * 查询经方
     */
    Typhoidtheoryprescription queryById(Long id);

    /**
     * 查询经方列表
     */
    TableDataInfo<Typhoidtheoryprescription> queryPageList(Typhoidtheoryprescription bo, PageQuery pageQuery);

    /**
     * 查询经方列表
     */
    List<Typhoidtheoryprescription> queryList(Typhoidtheoryprescription bo);

    /**
     * 新增经方
     */
    Boolean insert(Typhoidtheoryprescription bo);

    /**
     * 修改经方
     */
    Boolean update(Typhoidtheoryprescription bo);

    /**
     * 校验并批量删除经方信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
