package com.ketty.serviceimpl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ketty.api_entity.*;
import com.ketty.api_mapper.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ketty.common_base.MinioUtil;
import com.ketty.service.ChineseherbService;
import com.ketty.service.ChineseherbcategoryService;
import com.ketty.service.ChineseherbimageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ketty
 * @since 2023-01-15
 */
@Service
@DS("master")
public class ChineseherbServiceImpl extends ServiceImpl<ChineseherbMapper, Chineseherb> implements ChineseherbService {
    @Autowired
    ChineseherbcategoryService categoryService;
    @Autowired
    ChineseherbMapper mapper;
    @Autowired
    ChineseherbimageService imageService;
    @Autowired
    MinioUtil minioUtil;

    @Override
    public List<FlowCategory> getAll() {
        List<FlowCategory> flowCategoryList = new ArrayList<>();
        List<Chineseherbcategory> fatherList = categoryService.queryListByParent(0L);
        if (fatherList.size() > 0) {
            for (Chineseherbcategory father : fatherList) {
                FlowCategory flowCategory = new FlowCategory();
                flowCategory.setFatherTitle(father.getCategoryName());
                List<FlowCategory.ChildCategory> childCategoryList = new ArrayList<>();
                List<Chineseherbcategory> childList = categoryService.queryListByParent(father.getCategoryId());
                if (childList.size() > 0) {
                    for (Chineseherbcategory child: childList) {
                        FlowCategory.ChildCategory childCategory = new FlowCategory.ChildCategory();
                        childCategory.setChildTitle(child.getCategoryName());
                        List<String> tags = new ArrayList<>();
                        List<Chineseherb> chineseherbList = mapper.selectList(
                                new LambdaQueryWrapper<Chineseherb>()
                                        .eq(Chineseherb::getCategoryId,child.getCategoryId()));
                        for (Chineseherb chineseherb : chineseherbList) {
                            tags.add(chineseherb.getMedicinalName());
                        }
                        childCategory.setTagList(tags);
                        childCategoryList.add(childCategory);
                    }
                }
                flowCategory.setChildList(childCategoryList);
                flowCategoryList.add(flowCategory);
            }
        }
        return flowCategoryList;
    }

    @Override
    public ChineseherbAndImages getByName(String name) {
        Chineseherb chineseherb = mapper.selectOne(new LambdaQueryWrapper<Chineseherb>()
                .eq(Chineseherb::getMedicinalName,name));
        if (chineseherb != null) {
            List<String> images = imageService.queryByCbId(Long.valueOf(chineseherb.getId()));
            ChineseherbAndImages chineseherbAndImages = new ChineseherbAndImages();
            chineseherbAndImages.setChineseherb(chineseherb);
            chineseherbAndImages.setImages(images);
            return chineseherbAndImages;
        } else {
            throw new RuntimeException("该项暂无数据");
        }
    }

    @Override
    public List<ChineseherbAndImages> selectLikeName(String name) {
        List<Chineseherb> chineseherbList = mapper.selectList(new LambdaQueryWrapper<Chineseherb>()
                .select(Chineseherb::getId,Chineseherb::getMedicinalName,Chineseherb::getPinYin)
                .like(name != null,Chineseherb::getMedicinalName,name)
                .orderByAsc(Chineseherb::getId));
        if (chineseherbList != null && chineseherbList.size() > 0) {
            List<ChineseherbAndImages> list = new ArrayList<>();
            for (Chineseherb chineseherb : chineseherbList) {
                List<String> images = imageService.queryByCbId(Long.valueOf(chineseherb.getId()));
                ChineseherbAndImages chineseherbAndImages = new ChineseherbAndImages();
                chineseherbAndImages.setChineseherb(chineseherb);
                chineseherbAndImages.setImages(images);
                list.add(chineseherbAndImages);
            }
            return list;
        } else {
            throw new RuntimeException("无数据");
        }
    }

}
