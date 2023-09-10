package com.xlbc.app.service;

import com.xlbc.app.domain.Chineseherbimage;
import com.xlbc.common.core.page.TableDataInfo;
import com.xlbc.common.core.domain.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 中药图片Service接口
 *
 * @author ketty
 * @date 2023-03-20
 */
public interface IChineseherbimageService {

    /**
     * 查询中药图片
     */
    Chineseherbimage queryById(Long imgId);

    Chineseherbimage queryByCbId(Long cbId);

    /**
     * 新增中药图片
     */
    Boolean insert(Chineseherbimage bo);

    /**
     * 修改中药图片
     */
    Boolean update(Chineseherbimage bo);

    /**
     * 批量删除中药图片信息
     */
    Boolean deleteByCbIds(Collection<Long> ids);
}
