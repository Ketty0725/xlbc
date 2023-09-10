package com.xlbc.app.service;

import com.xlbc.app.domain.Typhoidtheoryprescriptionworkcited;
import com.xlbc.common.core.page.TableDataInfo;
import com.xlbc.common.core.domain.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 相关条文Service接口
 *
 * @author ketty
 * @date 2023-04-02
 */
public interface ITyphoidtheoryprescriptionworkcitedService {

    /**
     * 查询相关条文
     */
    Typhoidtheoryprescriptionworkcited queryById(Long id);

    List<Typhoidtheoryprescriptionworkcited> queryListByPreparationId(Long preparationId);

    List<Typhoidtheoryprescriptionworkcited> queryOneByPreparationId(Long preparationId);

    /**
     * 查询相关条文列表
     */
    TableDataInfo<Typhoidtheoryprescriptionworkcited> queryPageList(Typhoidtheoryprescriptionworkcited bo, PageQuery pageQuery);

    /**
     * 查询相关条文列表
     */
    List<Typhoidtheoryprescriptionworkcited> queryList(Typhoidtheoryprescriptionworkcited bo);

    /**
     * 新增相关条文
     */
    Boolean insert(Typhoidtheoryprescriptionworkcited bo);

    /**
     * 修改相关条文
     */
    Boolean update(Typhoidtheoryprescriptionworkcited bo);

    /**
     * 校验并批量删除相关条文信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    void deleteByPreparationId(Long preparationId);
}
