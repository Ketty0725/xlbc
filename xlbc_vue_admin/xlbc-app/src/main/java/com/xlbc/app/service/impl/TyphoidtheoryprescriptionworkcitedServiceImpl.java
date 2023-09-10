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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.xlbc.app.domain.Typhoidtheoryprescriptionworkcited;
import com.xlbc.app.mapper.TyphoidtheoryprescriptionworkcitedMapper;
import com.xlbc.app.service.ITyphoidtheoryprescriptionworkcitedService;

import java.util.List;
import java.util.Collection;

/**
 * 相关条文Service业务层处理
 *
 * @author ketty
 * @date 2023-04-02
 */
@RequiredArgsConstructor
@Service
@DS("slave")
public class TyphoidtheoryprescriptionworkcitedServiceImpl implements ITyphoidtheoryprescriptionworkcitedService {

    private final TyphoidtheoryprescriptionworkcitedMapper baseMapper;

    /**
     * 查询相关条文
     */
    @Override
    public Typhoidtheoryprescriptionworkcited queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    @Override
    public List<Typhoidtheoryprescriptionworkcited> queryListByPreparationId(Long preparationId) {
        LambdaQueryWrapper<Typhoidtheoryprescriptionworkcited> lqw = Wrappers.lambdaQuery();
        lqw.eq(preparationId != null, Typhoidtheoryprescriptionworkcited::getPreparationId, preparationId);
        return baseMapper.selectVoList(lqw);
    }

    @Override
    public List<Typhoidtheoryprescriptionworkcited> queryOneByPreparationId(Long preparationId) {
        LambdaQueryWrapper<Typhoidtheoryprescriptionworkcited> lqw = Wrappers.lambdaQuery();
        lqw.eq(preparationId != null, Typhoidtheoryprescriptionworkcited::getPreparationId, preparationId);
        lqw.last("limit 1");
        return baseMapper.selectVoList(lqw);
    }

    /**
     * 查询相关条文列表
     */
    @Override
    public TableDataInfo<Typhoidtheoryprescriptionworkcited> queryPageList(Typhoidtheoryprescriptionworkcited bo, PageQuery pageQuery) {
        LambdaQueryWrapper<Typhoidtheoryprescriptionworkcited> lqw = buildQueryWrapper(bo);
        Page<Typhoidtheoryprescriptionworkcited> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询相关条文列表
     */
    @Override
    public List<Typhoidtheoryprescriptionworkcited> queryList(Typhoidtheoryprescriptionworkcited bo) {
        LambdaQueryWrapper<Typhoidtheoryprescriptionworkcited> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<Typhoidtheoryprescriptionworkcited> buildQueryWrapper(Typhoidtheoryprescriptionworkcited bo) {
        LambdaQueryWrapper<Typhoidtheoryprescriptionworkcited> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getContent()), Typhoidtheoryprescriptionworkcited::getContent, bo.getContent());
        lqw.eq(StringUtils.isNotBlank(bo.getProvenance()), Typhoidtheoryprescriptionworkcited::getProvenance, bo.getProvenance());
        lqw.eq(bo.getType() != null, Typhoidtheoryprescriptionworkcited::getType, bo.getType());
        lqw.eq(bo.getPreparationId() != null, Typhoidtheoryprescriptionworkcited::getPreparationId, bo.getPreparationId());
        return lqw;
    }

    /**
     * 新增相关条文
     */
    @Override
    public Boolean insert(Typhoidtheoryprescriptionworkcited bo) {
        Typhoidtheoryprescriptionworkcited add = BeanUtil.toBean(bo, Typhoidtheoryprescriptionworkcited.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改相关条文
     */
    @Override
    public Boolean update(Typhoidtheoryprescriptionworkcited bo) {
        Typhoidtheoryprescriptionworkcited update = BeanUtil.toBean(bo, Typhoidtheoryprescriptionworkcited.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(Typhoidtheoryprescriptionworkcited entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除相关条文
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }

    @Override
    public void deleteByPreparationId(Long preparationId) {
        LambdaUpdateWrapper<Typhoidtheoryprescriptionworkcited> lqw = Wrappers.lambdaUpdate();
        lqw.eq(preparationId != null, Typhoidtheoryprescriptionworkcited::getPreparationId, preparationId);
        baseMapper.delete(lqw);
    }
}
