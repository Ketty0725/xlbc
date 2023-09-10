package com.xlbc.app.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
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
import com.xlbc.app.domain.Chineseherbimage;
import com.xlbc.app.mapper.ChineseherbimageMapper;
import com.xlbc.app.service.IChineseherbimageService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 中药图片Service业务层处理
 *
 * @author ketty
 * @date 2023-03-20
 */
@RequiredArgsConstructor
@Service
@DS("slave")
public class ChineseherbimageServiceImpl implements IChineseherbimageService {

    private final ChineseherbimageMapper baseMapper;

    private final ISysOssService ossService;

    /**
     * 查询中药图片
     */
    @Override
    public Chineseherbimage queryById(Long imgId){
        return baseMapper.selectById(imgId);
    }

    @Override
    public Chineseherbimage queryByCbId(Long cbId) {
        Chineseherbimage chineseherbimage = baseMapper.selectOne(
            new LambdaQueryWrapper<Chineseherbimage>()
                .eq(Chineseherbimage::getCbId,cbId));
        if (chineseherbimage != null) {
            String image = chineseherbimage.getOssIdList();
            SysOssVo oss = new SysOssVo();
            if (image != null && !"".equals(image)) {
                if (image.contains(",")) {
                    image = image.substring(0, image.indexOf(","));
                }
                oss = ossService.getById(Long.valueOf(image));
            }
            chineseherbimage.setImage(oss.getUrl());
        }
        return chineseherbimage;
    }

    /**
     * 新增中药图片
     */
    @Override
    public Boolean insert(Chineseherbimage bo) {
        Chineseherbimage add = BeanUtil.toBean(bo, Chineseherbimage.class);
        boolean flag = baseMapper.insert(add) > 0;
        return flag;
    }

    /**
     * 修改中药图片
     */
    @Override
    public Boolean update(Chineseherbimage bo) {
        Chineseherbimage update = BeanUtil.toBean(bo, Chineseherbimage.class);
        return baseMapper.updateById(update) > 0;
    }

    @Override
    public Boolean deleteByCbIds(Collection<Long> ids) {
        for (Long id: ids) {
            Chineseherbimage chineseherbimage = baseMapper.selectOne(
                new LambdaQueryWrapper<Chineseherbimage>()
                    .eq(Chineseherbimage::getCbId,id));
            if (chineseherbimage != null && StringUtils.isNotBlank(chineseherbimage.getOssIdList())) {
                Collection<Long> ossIds = new ArrayList<>();
                String images = chineseherbimage.getOssIdList();
                if (images.contains(",")) {
                    String[] split = images.split(",");
                    for (String str: split) {
                        ossIds.add(Long.valueOf(str));
                    }
                } else {
                    ossIds.add(Long.valueOf(images));
                }
                ossService.deleteWithValidByIds(ossIds, true);
                baseMapper.deleteById(chineseherbimage);
            }
        }
        return null;
    }

}
