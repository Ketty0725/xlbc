package com.ketty.serviceimpl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ketty.api_entity.Chineseherbimage;
import com.ketty.api_mapper.ChineseherbimageMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ketty.api_entity.SysOss;
import com.ketty.service.SysOssService;
import com.ketty.service.ChineseherbimageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ketty.common_utils.StringUtils;

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
public class ChineseherbimageServiceImpl extends ServiceImpl<ChineseherbimageMapper, Chineseherbimage> implements ChineseherbimageService {
    @Autowired
    ChineseherbimageMapper chineseherbimageMapper;
    @Autowired
    SysOssService sysOssService;

    @Override
    public List<String> queryByCbId(Long cbId) {
        Chineseherbimage chineseherbimage = chineseherbimageMapper.selectOne(
                new LambdaQueryWrapper<Chineseherbimage>()
                        .eq(Chineseherbimage::getCbId,cbId));
        if (chineseherbimage != null) {
            String ossIdList = chineseherbimage.getOssIdList();
            List<String> images = new ArrayList<>();
            if (StringUtils.isNotBlank(ossIdList)) {
                if (ossIdList.contains(",")) {
                    for (String ossId : StringUtils.splitList(ossIdList)) {
                        SysOss oss = sysOssService.getById(Long.valueOf(ossId));
                        images.add(oss.getUrl());
                    }
                } else {
                    SysOss oss = sysOssService.getById(Long.valueOf(ossIdList));
                    images.add(oss.getUrl());
                }
                return images;
            }
        }
        return null;
    }
}
