package com.ketty.serviceimpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ketty.api_entity.Chineseherbcategory;
import com.ketty.api_entity.Prescriptioncategory;
import com.ketty.api_mapper.ChineseherbcategoryMapper;
import com.ketty.api_mapper.PrescriptioncategoryMapper;
import com.ketty.service.PrescriptioncategoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ketty
 * @since 2023-04-04
 */
@Service
public class PrescriptioncategoryServiceImpl extends ServiceImpl<PrescriptioncategoryMapper, Prescriptioncategory> implements PrescriptioncategoryService {
    @Autowired
    PrescriptioncategoryMapper baseMapper;

    @Override
    public List<Prescriptioncategory> queryListByParent(Long parentId) {
        LambdaQueryWrapper<Prescriptioncategory> lqw = Wrappers.lambdaQuery();
        lqw.select(Prescriptioncategory::getCategoryId,Prescriptioncategory::getCategoryName);
        lqw.eq(Prescriptioncategory::getParentId,parentId);
        return baseMapper.selectList(lqw);
    }

    @Override
    public Prescriptioncategory queryByName(String categoryName) {
        LambdaQueryWrapper<Prescriptioncategory> lqw = Wrappers.lambdaQuery();
        lqw.select(Prescriptioncategory::getCategoryId);
        lqw.eq(Prescriptioncategory::getCategoryName,categoryName);
        return baseMapper.selectOne(lqw);
    }
}
