package com.xlbc.app.service;

import com.xlbc.app.domain.Typhoidtheorychineseherb;
import com.xlbc.common.core.page.TableDataInfo;
import com.xlbc.common.core.domain.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 中药Service接口
 *
 * @author ketty
 * @date 2023-04-02
 */
public interface ITyphoidtheorychineseherbService {

    /**
     * 查询中药
     */
    Typhoidtheorychineseherb queryById(Long id);

    /**
     * 查询中药列表
     */
    TableDataInfo<Typhoidtheorychineseherb> queryPageList(Typhoidtheorychineseherb bo, PageQuery pageQuery);

    /**
     * 查询中药列表
     */
    List<Typhoidtheorychineseherb> queryList(Typhoidtheorychineseherb bo);

    /**
     * 新增中药
     */
    Boolean insert(Typhoidtheorychineseherb bo);

    /**
     * 修改中药
     */
    Boolean update(Typhoidtheorychineseherb bo);

    /**
     * 校验并批量删除中药信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
