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
import com.xlbc.app.domain.Homebanner;
import com.xlbc.app.mapper.HomebannerMapper;
import com.xlbc.app.service.IHomebannerService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 轮播图Service业务层处理
 *
 * @author ketty
 * @date 2023-04-04
 */
@RequiredArgsConstructor
@Service
@DS("slave")
public class HomebannerServiceImpl implements IHomebannerService {

    private final HomebannerMapper baseMapper;

    private final ISysOssService ossService;

    /**
     * 查询轮播图
     */
    @Override
    public Homebanner queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询轮播图列表
     */
    @Override
    public TableDataInfo<Homebanner> queryPageList(Homebanner bo, PageQuery pageQuery) {
        LambdaQueryWrapper<Homebanner> lqw = buildQueryWrapper(bo);
        Page<Homebanner> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        for (Homebanner bean : result.getRecords()) {
            String ossId = bean.getOssId();
            if (StringUtils.isNotBlank(ossId)) {
                SysOssVo oss = ossService.getById(Long.valueOf(ossId));
                bean.setImage(oss.getUrl());
            }
        }
        return TableDataInfo.build(result);
    }

    /**
     * 查询轮播图列表
     */
    @Override
    public List<Homebanner> queryList(Homebanner bo) {
        LambdaQueryWrapper<Homebanner> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<Homebanner> buildQueryWrapper(Homebanner bo) {
        LambdaQueryWrapper<Homebanner> lqw = Wrappers.lambdaQuery();
        lqw.orderByAsc(Homebanner::getOrderNum);
        return lqw;
    }

    /**
     * 新增轮播图
     */
    @Override
    public Boolean insert(Homebanner bo) {
        Homebanner add = BeanUtil.toBean(bo, Homebanner.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改轮播图
     */
    @Override
    public Boolean update(Homebanner bo) {
        Homebanner update = BeanUtil.toBean(bo, Homebanner.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(Homebanner entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除轮播图
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            for (Long id: ids) {
                Homebanner bean = baseMapper.selectById(id);
                if (bean == null) {
                    Collection<Long> ossIds = new ArrayList<>();
                    ossIds.add(Long.valueOf(bean.getOssId()));
                    ossService.deleteWithValidByIds(ossIds, true);
                }
            }
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
