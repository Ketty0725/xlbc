package com.xlbc.app.service;

import com.xlbc.app.domain.Productbasicinfo;
import com.xlbc.common.core.page.TableDataInfo;
import com.xlbc.common.core.domain.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 药品说明Service接口
 *
 * @author ketty
 * @date 2023-03-31
 */
public interface IProductbasicinfoService {

    /**
     * 查询药品说明
     */
    Productbasicinfo queryById(Long id);

    /**
     * 查询药品说明列表
     */
    TableDataInfo<Productbasicinfo> queryPageList(Productbasicinfo bo, PageQuery pageQuery);

    /**
     * 查询药品说明列表
     */
    List<Productbasicinfo> queryList(Productbasicinfo bo);

    /**
     * 新增药品说明
     */
    Boolean insert(Productbasicinfo bo);

    /**
     * 修改药品说明
     */
    Boolean update(Productbasicinfo bo);

    /**
     * 校验并批量删除药品说明信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
