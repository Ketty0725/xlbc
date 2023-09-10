package com.xlbc.app.service;

import com.xlbc.app.domain.Community;
import com.xlbc.common.core.domain.PageQuery;
import com.xlbc.common.core.page.TableDataInfo;

import java.util.Collection;
import java.util.List;

/**
 * 笔记Service接口
 *
 * @author ketty
 * @date 2023-03-26
 */
public interface ICommunityService {

    /**
     * 查询笔记
     */
    Community queryById(Long id);

    /**
     * 查询笔记列表
     */
    TableDataInfo<Community> queryPageList(Community bo, String uName, PageQuery pageQuery);

    /**
     * 查询笔记列表
     */
    List<Community> queryList(Community bo);

    /**
     * 新增笔记
     */
    Boolean insert(Community bo);

    /**
     * 修改笔记
     */
    Boolean update(Community bo);

    /**
     * 校验并批量删除笔记信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
