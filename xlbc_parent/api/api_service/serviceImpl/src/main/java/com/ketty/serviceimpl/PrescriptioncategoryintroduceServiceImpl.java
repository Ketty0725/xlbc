package com.ketty.serviceimpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ketty.api_entity.Chineseherbcategory;
import com.ketty.api_entity.Chineseherbcategoryintroduce;
import com.ketty.api_entity.Prescriptioncategory;
import com.ketty.api_entity.Prescriptioncategoryintroduce;
import com.ketty.api_mapper.ChineseherbcategoryintroduceMapper;
import com.ketty.api_mapper.PrescriptioncategoryintroduceMapper;
import com.ketty.service.ChineseherbcategoryService;
import com.ketty.service.PrescriptioncategoryService;
import com.ketty.service.PrescriptioncategoryintroduceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ketty
 * @since 2023-04-04
 */
@Service
public class PrescriptioncategoryintroduceServiceImpl extends ServiceImpl<PrescriptioncategoryintroduceMapper, Prescriptioncategoryintroduce> implements PrescriptioncategoryintroduceService {
    @Autowired
    PrescriptioncategoryintroduceMapper mapper;
    @Autowired
    PrescriptioncategoryService categoryService;

    @Override
    public Prescriptioncategoryintroduce getByTitle(String title) {
        Prescriptioncategory category = categoryService.queryByName(title);
        if (category != null) {
            LambdaQueryWrapper<Prescriptioncategoryintroduce> lqw = Wrappers.lambdaQuery();
            lqw.eq(Prescriptioncategoryintroduce::getCategoryId,category.getCategoryId());
            return mapper.selectOne(lqw);
        }
        return null;
    }
}
