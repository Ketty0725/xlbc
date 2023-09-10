package com.xlbc.app.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.xlbc.app.domain.Chineseherbimage;
import com.xlbc.common.core.page.TableDataInfo;
import com.xlbc.common.core.domain.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xlbc.common.utils.StringUtils;
import com.xlbc.system.domain.vo.SysOssVo;
import com.xlbc.system.service.ISysOssService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.xlbc.app.domain.Productimage;
import com.xlbc.app.mapper.ProductimageMapper;
import com.xlbc.app.service.IProductimageService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 药品图片Service业务层处理
 *
 * @author ketty
 * @date 2023-04-01
 */
@RequiredArgsConstructor
@Service
@DS("slave")
public class ProductimageServiceImpl implements IProductimageService {

    private final ProductimageMapper baseMapper;

    private final ISysOssService ossService;

    /**
     * 查询药品图片
     */
    @Override
    public Productimage queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    @Override
    public Productimage queryByProductId(Long productId) {
        Productimage productimage = baseMapper.selectOne(
            new LambdaQueryWrapper<Productimage>()
                .eq(Productimage::getProductId,productId));
        if (productimage != null) {
            String image = productimage.getOssIdList();
            SysOssVo oss = new SysOssVo();
            if (image != null && !"".equals(image)) {
                if (image.contains(",")) {
                    image = image.substring(0, image.indexOf(","));
                }
                oss = ossService.getById(Long.valueOf(image));
            }
            productimage.setImage(oss.getUrl());
        }
        return productimage;
    }

    /**
     * 新增药品图片
     */
    @Override
    public Boolean insert(Productimage bo) {
        Productimage add = BeanUtil.toBean(bo, Productimage.class);
        boolean flag = baseMapper.insert(add) > 0;
        return flag;
    }

    /**
     * 修改药品图片
     */
    @Override
    public Boolean update(Productimage bo) {
        Productimage update = BeanUtil.toBean(bo, Productimage.class);
        return baseMapper.updateById(update) > 0;
    }

    @Override
    public Boolean deleteByByProductIds(Collection<Long> ids) {
        for (Long id: ids) {
            Productimage productimage = baseMapper.selectOne(
                new LambdaQueryWrapper<Productimage>()
                    .eq(Productimage::getProductId,id));
            if (productimage != null && StringUtils.isNotBlank(productimage.getOssIdList())) {
                Collection<Long> ossIds = new ArrayList<>();
                String images = productimage.getOssIdList();
                if (images.contains(",")) {
                    String[] split = images.split(",");
                    for (String str: split) {
                        ossIds.add(Long.valueOf(str));
                    }
                } else {
                    ossIds.add(Long.valueOf(images));
                }
                ossService.deleteWithValidByIds(ossIds, true);
                baseMapper.deleteById(productimage);
            }
        }
        return null;
    }
}
