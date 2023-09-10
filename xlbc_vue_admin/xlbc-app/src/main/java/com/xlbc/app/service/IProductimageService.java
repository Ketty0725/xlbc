package com.xlbc.app.service;

import com.xlbc.app.domain.Productimage;
import com.xlbc.common.core.domain.PageQuery;
import com.xlbc.common.core.page.TableDataInfo;

import java.util.Collection;
import java.util.List;

/**
 * 药品图片Service接口
 *
 * @author ketty
 * @date 2023-04-01
 */
public interface IProductimageService {

    /**
     * 查询药品图片
     */
    Productimage queryById(Long id);

    Productimage queryByProductId(Long productId);

    /**
     * 新增药品图片
     */
    Boolean insert(Productimage bo);

    /**
     * 修改药品图片
     */
    Boolean update(Productimage bo);

    /**
     * 校验并批量删除药品图片信息
     */
    Boolean deleteByByProductIds(Collection<Long> ids);
}
