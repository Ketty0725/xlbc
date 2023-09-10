package com.ketty.serviceimpl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ketty.api_entity.Homebanner;
import com.ketty.api_entity.Prescriptioncategory;
import com.ketty.api_entity.SysOss;
import com.ketty.api_mapper.HomebannerMapper;
import com.ketty.service.HomebannerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ketty.service.SysOssService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
@DS("master")
public class HomebannerServiceImpl extends ServiceImpl<HomebannerMapper, Homebanner> implements HomebannerService {
    @Autowired
    HomebannerMapper mapper;
    @Autowired
    SysOssService sysOssService;

    @Override
    public List<String> get() {
        LambdaQueryWrapper<Homebanner> lqw = Wrappers.lambdaQuery();
        lqw.select(Homebanner::getOssId);
        lqw.orderByAsc(Homebanner::getOrderNum);
        List<Homebanner> list = mapper.selectList(lqw);
        List<String> images = new ArrayList<>();
        if (list.size() > 0) {
            for (Homebanner bean : list) {
                SysOss oss = sysOssService.getById(Long.valueOf(bean.getOssId()));
                images.add(oss.getUrl());
            }
        }
        return images;
    }
}
