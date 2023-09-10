package com.ketty.service;

import com.ketty.api_entity.Chineseherbcategory;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ketty
 * @since 2023-04-04
 */
public interface ChineseherbcategoryService extends IService<Chineseherbcategory> {

    List<Chineseherbcategory> queryListByParent(Long parentId);

    Chineseherbcategory queryByName(String categoryName);
}
