package com.xlbc.app.service;

import com.xlbc.app.domain.Typhoidtheorychineseherbcontent;
import com.xlbc.app.domain.Typhoidtheoryprescriptionworkcited;
import com.xlbc.common.core.page.TableDataInfo;
import com.xlbc.common.core.domain.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 中药内容Service接口
 *
 * @author ketty
 * @date 2023-04-02
 */
public interface ITyphoidtheorychineseherbcontentService {

    /**
     * 查询中药内容
     */
    Typhoidtheorychineseherbcontent queryById(Long id);

    /**
     * 查询中药内容列表
     */
    TableDataInfo<Typhoidtheorychineseherbcontent> queryPageList(Typhoidtheorychineseherbcontent bo, PageQuery pageQuery);

    /**
     * 查询中药内容列表
     */
    List<Typhoidtheorychineseherbcontent> queryList(Typhoidtheorychineseherbcontent bo);

    List<Typhoidtheorychineseherbcontent> queryList(String sql);

    /**
     * 新增中药内容
     */
    Boolean insert(Typhoidtheorychineseherbcontent bo);

    /**
     * 修改中药内容
     */
    Boolean update(Typhoidtheorychineseherbcontent bo);

    /**
     * 校验并批量删除中药内容信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    void deleteById(Long id);

    void deleteByCbId(Long cbId);
}
