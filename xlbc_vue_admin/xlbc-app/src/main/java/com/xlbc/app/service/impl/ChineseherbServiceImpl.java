package com.xlbc.app.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.xlbc.app.domain.Chineseherbimage;
import com.xlbc.app.service.IChineseherbcategoryService;
import com.xlbc.app.service.IChineseherbimageService;
import com.xlbc.common.core.page.TableDataInfo;
import com.xlbc.common.core.domain.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xlbc.common.utils.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.xlbc.app.domain.Chineseherb;
import com.xlbc.app.mapper.ChineseherbMapper;
import com.xlbc.app.service.IChineseherbService;

import java.util.List;
import java.util.Collection;

/**
 * 中药数据Service业务层处理
 *
 * @author ketty
 * @date 2023-03-20
 */
@RequiredArgsConstructor
@Service
@DS("slave")
public class ChineseherbServiceImpl implements IChineseherbService {

    private final ChineseherbMapper baseMapper;

    private final IChineseherbcategoryService categoryService;

    private final IChineseherbimageService imageService;

    /**
     * 查询中药数据
     */
    @Override
    public Chineseherb queryById(Long id) {
        Chineseherb chineseherb = baseMapper.selectById(id);
        Chineseherbimage chineseherbimage = imageService.queryByCbId(chineseherb.getId());
        chineseherb.setImages(chineseherbimage);
        return chineseherb;
    }

    /**
     * 查询中药数据列表
     */
    @Override
    public TableDataInfo<Chineseherb> queryPageList(Chineseherb bean, PageQuery pageQuery) {
        LambdaQueryWrapper<Chineseherb> lqw = buildQueryWrapper(bean);
        Page<Chineseherb> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        for (int i = 0; i < result.getRecords().size(); i++) {
            Chineseherbimage chineseherbimage = imageService.queryByCbId(
                result.getRecords().get(i).getId());
            result.getRecords().get(i).setImages(chineseherbimage);
        }
        return TableDataInfo.build(result);
    }

    /**
     * 查询中药数据列表
     */
    @Override
    public List<Chineseherb> queryList(Chineseherb bean) {
        LambdaQueryWrapper<Chineseherb> lqw = buildQueryWrapper(bean);
        List<Chineseherb> list = baseMapper.selectVoList(lqw);
        for (int i = 0; i < list.size(); i++) {
            Chineseherbimage chineseherbimage = imageService.queryByCbId(list.get(i).getId());
            list.get(i).setImages(chineseherbimage);
        }
        return list;
    }

    private LambdaQueryWrapper<Chineseherb> buildQueryWrapper(Chineseherb bean) {
        LambdaQueryWrapper<Chineseherb> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.isNotBlank(bean.getMedicinalName()), Chineseherb::getMedicinalName, bean.getMedicinalName());
        if (bean.getCategoryId() != null) {
            lqw.eq(Chineseherb::getCategoryId, bean.getCategoryId());
        }
        lqw.like(StringUtils.isNotBlank(bean.getSexualTaste()), Chineseherb::getSexualTaste, bean.getSexualTaste());
        lqw.like(StringUtils.isNotBlank(bean.getEfficacy()), Chineseherb::getEfficacy, bean.getEfficacy());
        return lqw;
    }

    /**
     * 新增中药数据
     */
    @Override
    public Boolean insert(Chineseherb bean) {
        Chineseherb add = BeanUtil.toBean(bean, Chineseherb.class);
        add.setCategory(selectCategoryName(add.getCategoryId()));
        boolean flag = baseMapper.insert(add) > 0;
        Chineseherbimage chineseherbimage = add.getImages();
        chineseherbimage.setCbId(add.getId());
        imageService.insert(chineseherbimage);
        return flag;
    }

    /**
     * 修改中药数据
     */
    @Override
    public Boolean update(Chineseherb bean) {
        Chineseherb update = BeanUtil.toBean(bean, Chineseherb.class);
        update.setCategory(selectCategoryName(update.getCategoryId()));
        imageService.update(update.getImages());
        return baseMapper.updateById(update) > 0;
    }

    public String selectCategoryName(Long categoryId) {
        return categoryService.selectCategoryName(categoryId);
    }

    /**
     * 批量删除中药数据
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        imageService.deleteByCbIds(ids);
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
