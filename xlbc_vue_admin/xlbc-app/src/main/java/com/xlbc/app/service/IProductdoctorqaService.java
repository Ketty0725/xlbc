package com.xlbc.app.service;

import com.xlbc.app.domain.Productdoctorqa;
import com.xlbc.common.core.page.TableDataInfo;
import com.xlbc.common.core.domain.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 医生问答Service接口
 *
 * @author ketty
 * @date 2023-04-01
 */
public interface IProductdoctorqaService {

    /**
     * 查询医生问答
     */
    Productdoctorqa queryById(Long id);

    /**
     * 查询医生问答列表
     */
    TableDataInfo<Productdoctorqa> queryPageList(Productdoctorqa bo, PageQuery pageQuery);

    /**
     * 查询医生问答列表
     */
    List<Productdoctorqa> queryList(Productdoctorqa bo);

    /**
     * 新增医生问答
     */
    Boolean insert(Productdoctorqa bo);

    /**
     * 修改医生问答
     */
    Boolean update(Productdoctorqa bo);

    /**
     * 校验并批量删除医生问答信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
