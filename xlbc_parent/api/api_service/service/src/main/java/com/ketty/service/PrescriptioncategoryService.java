package com.ketty.service;

import com.ketty.api_entity.Chineseherbcategory;
import com.ketty.api_entity.Prescriptioncategory;
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
public interface PrescriptioncategoryService extends IService<Prescriptioncategory> {

    List<Prescriptioncategory> queryListByParent(Long parentId);

    Prescriptioncategory queryByName(String categoryName);
}
