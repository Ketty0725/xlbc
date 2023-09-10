package com.xlbc.app.service;

import com.xlbc.app.domain.Chinesepatentmedicine;
import com.xlbc.common.core.page.TableDataInfo;
import com.xlbc.common.core.domain.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 中成药数据Service接口
 *
 * @author ketty
 * @date 2023-04-02
 */
public interface IChinesepatentmedicineService {

    /**
     * 查询中成药数据
     */
    Chinesepatentmedicine queryById(Long id);

    /**
     * 查询中成药数据列表
     */
    TableDataInfo<Chinesepatentmedicine> queryPageList(Chinesepatentmedicine bo, PageQuery pageQuery);

    /**
     * 查询中成药数据列表
     */
    List<Chinesepatentmedicine> queryList(Chinesepatentmedicine bo);

    /**
     * 新增中成药数据
     */
    Boolean insert(Chinesepatentmedicine bo);

    /**
     * 修改中成药数据
     */
    Boolean update(Chinesepatentmedicine bo);

    /**
     * 校验并批量删除中成药数据信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
