package com.xlbc.app.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.xlbc.app.domain.Typhoidtheoryprescriptionworkcited;
import com.xlbc.common.core.page.TableDataInfo;
import com.xlbc.common.core.domain.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xlbc.common.utils.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.xlbc.app.domain.Typhoidtheorychineseherbcontent;
import com.xlbc.app.mapper.TyphoidtheorychineseherbcontentMapper;
import com.xlbc.app.service.ITyphoidtheorychineseherbcontentService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 中药内容Service业务层处理
 *
 * @author ketty
 * @date 2023-04-02
 */
@RequiredArgsConstructor
@Service
@DS("slave")
public class TyphoidtheorychineseherbcontentServiceImpl implements ITyphoidtheorychineseherbcontentService {

    private final TyphoidtheorychineseherbcontentMapper baseMapper;

    /**
     * 查询中药内容
     */
    @Override
    public Typhoidtheorychineseherbcontent queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询中药内容列表
     */
    @Override
    public TableDataInfo<Typhoidtheorychineseherbcontent> queryPageList(Typhoidtheorychineseherbcontent bo, PageQuery pageQuery) {
        LambdaQueryWrapper<Typhoidtheorychineseherbcontent> lqw = buildQueryWrapper(bo);
        Page<Typhoidtheorychineseherbcontent> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询中药内容列表
     */
    @Override
    public List<Typhoidtheorychineseherbcontent> queryList(Typhoidtheorychineseherbcontent bo) {
        LambdaQueryWrapper<Typhoidtheorychineseherbcontent> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    @Override
    public List<Typhoidtheorychineseherbcontent> queryList(String sql) {
        LambdaQueryWrapper<Typhoidtheorychineseherbcontent> lqw = Wrappers.lambdaQuery();
        lqw.last(sql);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<Typhoidtheorychineseherbcontent> buildQueryWrapper(Typhoidtheorychineseherbcontent bo) {
        LambdaQueryWrapper<Typhoidtheorychineseherbcontent> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getCbId() != null, Typhoidtheorychineseherbcontent::getCbId, bo.getCbId());
        lqw.eq(bo.getTypeId() != null, Typhoidtheorychineseherbcontent::getTypeId, bo.getTypeId());
        lqw.eq(StringUtils.isNotBlank(bo.getContent()), Typhoidtheorychineseherbcontent::getContent, bo.getContent());
        return lqw;
    }

    /**
     * 新增中药内容
     */
    @Override
    public Boolean insert(Typhoidtheorychineseherbcontent bo) {
        Typhoidtheorychineseherbcontent add = BeanUtil.toBean(bo, Typhoidtheorychineseherbcontent.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改中药内容
     */
    @Override
    public Boolean update(Typhoidtheorychineseherbcontent bo) {
        Typhoidtheorychineseherbcontent update = BeanUtil.toBean(bo, Typhoidtheorychineseherbcontent.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(Typhoidtheorychineseherbcontent entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除中药内容
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }

    @Override
    public void deleteById(Long id) {
        baseMapper.deleteById(id);
    }

    @Override
    public void deleteByCbId(Long cbId) {
        LambdaUpdateWrapper<Typhoidtheorychineseherbcontent> lqw = Wrappers.lambdaUpdate();
        lqw.eq(cbId != null, Typhoidtheorychineseherbcontent::getCbId, cbId);
        baseMapper.delete(lqw);
    }
}
