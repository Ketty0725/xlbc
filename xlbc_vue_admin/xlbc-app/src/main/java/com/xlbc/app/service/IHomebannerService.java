package com.xlbc.app.service;

import com.xlbc.app.domain.Homebanner;
import com.xlbc.common.core.page.TableDataInfo;
import com.xlbc.common.core.domain.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 轮播图Service接口
 *
 * @author ketty
 * @date 2023-04-04
 */
public interface IHomebannerService {

    /**
     * 查询轮播图
     */
    Homebanner queryById(Long id);

    /**
     * 查询轮播图列表
     */
    TableDataInfo<Homebanner> queryPageList(Homebanner bo, PageQuery pageQuery);

    /**
     * 查询轮播图列表
     */
    List<Homebanner> queryList(Homebanner bo);

    /**
     * 新增轮播图
     */
    Boolean insert(Homebanner bo);

    /**
     * 修改轮播图
     */
    Boolean update(Homebanner bo);

    /**
     * 校验并批量删除轮播图信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
