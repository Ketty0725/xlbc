package com.ketty.service;

import com.ketty.api_entity.Productcategory;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ketty
 * @since 2023-04-05
 */
public interface ProductcategoryService extends IService<Productcategory> {

    List<Productcategory> getListByFather();

    List<Productcategory> getListByChild(Long parentId);

}
