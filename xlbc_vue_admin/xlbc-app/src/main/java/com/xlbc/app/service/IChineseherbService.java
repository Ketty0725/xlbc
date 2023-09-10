package com.xlbc.app.service;

import com.xlbc.app.domain.Chineseherb;
import com.xlbc.common.core.page.TableDataInfo;
import com.xlbc.common.core.domain.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 中药数据Service接口
 *
 * @author ketty
 * @date 2023-03-20
 */
public interface IChineseherbService {

    /**
     * 查询中药数据
     */
    Chineseherb queryById(Long id);

    /**
     * 查询中药数据列表
     */
    TableDataInfo<Chineseherb> queryPageList(Chineseherb bean, PageQuery pageQuery);

    /**
     * 查询中药数据列表
     */
    List<Chineseherb> queryList(Chineseherb bean);

    /**
     * 新增中药数据
     */
    Boolean insert(Chineseherb bean);

    /**
     * 修改中药数据
     */
    Boolean update(Chineseherb bean);

    /**
     * 校验并批量删除中药数据信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
