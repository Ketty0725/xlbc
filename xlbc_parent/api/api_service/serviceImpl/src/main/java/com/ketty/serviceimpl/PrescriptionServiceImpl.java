package com.ketty.serviceimpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ketty.api_entity.*;
import com.ketty.api_mapper.PrescriptionMapper;
import com.ketty.api_mapper.PrescriptionclassicMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ketty.service.ChineseherbcategoryService;
import com.ketty.service.PrescriptionService;
import com.ketty.service.PrescriptioncategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.Collator;
import java.util.*;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ketty
 * @since 2023-01-15
 */
@Service
public class PrescriptionServiceImpl extends ServiceImpl<PrescriptionMapper, Prescription> implements PrescriptionService {
    @Autowired
    PrescriptioncategoryService categoryService;
    @Autowired
    PrescriptionMapper mapper;
    @Autowired
    PrescriptionclassicMapper classicMapper;

    @Override
    public List<FlowCategory> getAllCommon() {
        List<FlowCategory> flowCategoryList = new ArrayList<>();
        List<Prescriptioncategory> fatherList = categoryService.queryListByParent(0L);
        if (fatherList.size() > 0) {
            for (Prescriptioncategory father : fatherList) {
                FlowCategory flowCategory = new FlowCategory();
                flowCategory.setFatherTitle(father.getCategoryName());
                List<FlowCategory.ChildCategory> childCategoryList = new ArrayList<>();
                List<Prescriptioncategory> childList = categoryService.queryListByParent(father.getCategoryId());
                if (childList.size() > 0) {
                    for (Prescriptioncategory child : childList) {
                        FlowCategory.ChildCategory childCategory = new FlowCategory.ChildCategory();
                        childCategory.setChildTitle(child.getCategoryName());
                        List<String> tags = new ArrayList<>();
                        List<Prescription> prescriptionList = mapper.selectList(
                                new LambdaQueryWrapper<Prescription>()
                                        .eq(Prescription::getCategoryId, child.getCategoryId()));
                        for (Prescription prescription : prescriptionList) {
                            tags.add(prescription.getPrescriptionName());
                        }
                        childCategory.setTagList(tags);
                        childCategoryList.add(childCategory);
                    }
                    flowCategory.setChildList(childCategoryList);
                    flowCategoryList.add(flowCategory);
                }
            }
        }
        return flowCategoryList;
    }

    @Override
    public Prescription getByName(String name) {
        Prescription prescription = mapper.selectOne(new LambdaQueryWrapper<Prescription>()
                .eq(Prescription::getPrescriptionName, name));
        if (prescription != null) {
            return prescription;
        } else {
            throw new RuntimeException("该项暂无数据");
        }
    }

    @Override
    public List<Prescription> selectLikeName(String name) {
        List<Prescription> prescriptionList = mapper.selectList(new LambdaQueryWrapper<Prescription>()
                .select(Prescription::getPrescriptionName, Prescription::getProvenance)
                .like(name != null, Prescription::getPrescriptionName, name)
                .orderByAsc(Prescription::getId));
        if (prescriptionList != null && prescriptionList.size() > 0) {
            return prescriptionList;
        } else {
            throw new RuntimeException("无数据");
        }
    }

    @Override
    public List<Prescription> getAllClassic() {
        List<Prescriptionclassic> classicList = classicMapper.selectList(null);
        LambdaQueryWrapper<Prescription> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(Prescription::getPrescriptionName, Prescription::getProvenance);
        for (Prescriptionclassic classic : classicList) {
            String name = classic.getName();
            wrapper.like(name != null, Prescription::getProvenance, name);
            wrapper.or();
        }

        List<Prescription> list = mapper.selectList(wrapper);
        Collections.sort(list, (l1, l2) -> {
            Collator instance = Collator.getInstance(Locale.CHINA);
            return instance.compare(l1.getPrescriptionName(), l2.getPrescriptionName());
        });
        return list;
    }

}
