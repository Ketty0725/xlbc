package com.xlbc.app.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.xlbc.app.domain.Typhoidtheorychineseherbcontent;
import com.xlbc.app.domain.Typhoidtheoryprescriptionworkcited;
import com.xlbc.app.service.ITyphoidtheoryprescriptionworkcitedService;
import com.xlbc.common.core.page.TableDataInfo;
import com.xlbc.common.core.domain.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xlbc.common.utils.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.xlbc.app.domain.Typhoidtheoryprescription;
import com.xlbc.app.mapper.TyphoidtheoryprescriptionMapper;
import com.xlbc.app.service.ITyphoidtheoryprescriptionService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 经方Service业务层处理
 *
 * @author ketty
 * @date 2023-04-02
 */
@RequiredArgsConstructor
@Service
@DS("slave")
public class TyphoidtheoryprescriptionServiceImpl implements ITyphoidtheoryprescriptionService {

    private final TyphoidtheoryprescriptionMapper baseMapper;

    private final ITyphoidtheoryprescriptionworkcitedService workcitedService;

    /**
     * 查询经方
     */
    @Override
    public Typhoidtheoryprescription queryById(Long id){
        Typhoidtheoryprescription bean = baseMapper.selectVoById(id);
        List<Typhoidtheoryprescriptionworkcited> list = workcitedService.queryListByPreparationId(id);
        bean.setWorkCites(list);
        return bean;
    }

    /**
     * 查询经方列表
     */
    @Override
    public TableDataInfo<Typhoidtheoryprescription> queryPageList(Typhoidtheoryprescription bo, PageQuery pageQuery) {
        LambdaQueryWrapper<Typhoidtheoryprescription> lqw = buildQueryWrapper(bo);
        Page<Typhoidtheoryprescription> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        for (Typhoidtheoryprescription bean : result.getRecords()) {
            List<Typhoidtheoryprescriptionworkcited> list = workcitedService.queryOneByPreparationId(bean.getId());
            bean.setWorkCites(list);
        }
        return TableDataInfo.build(result);
    }

    /**
     * 查询经方列表
     */
    @Override
    public List<Typhoidtheoryprescription> queryList(Typhoidtheoryprescription bo) {
        LambdaQueryWrapper<Typhoidtheoryprescription> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<Typhoidtheoryprescription> buildQueryWrapper(Typhoidtheoryprescription bo) {
        LambdaQueryWrapper<Typhoidtheoryprescription> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.isNotBlank(bo.getName()), Typhoidtheoryprescription::getName, bo.getName());
        return lqw;
    }

    /**
     * 新增经方
     */
    @Override
    public Boolean insert(Typhoidtheoryprescription bo) {
        Typhoidtheoryprescription add = BeanUtil.toBean(bo, Typhoidtheoryprescription.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
            if (add.getWorkCites().size() > 0) {
                for (Typhoidtheoryprescriptionworkcited workcited : add.getWorkCites()) {
                    if (StringUtils.isNotBlank(workcited.getContent()) ||
                        StringUtils.isNotBlank(workcited.getProvenance())) {
                        workcited.setPreparationId(bo.getId());
                        workcitedService.insert(workcited);
                    }
                }
            }
        }
        return flag;
    }

    /**
     * 修改经方
     */
    @Override
    public Boolean update(Typhoidtheoryprescription bo) {
        Typhoidtheoryprescription update = BeanUtil.toBean(bo, Typhoidtheoryprescription.class);
        validEntityBeforeSave(update);
        boolean flag = baseMapper.updateById(update) > 0;
        if (flag) {
            compareUpdateData(update.getWorkCites(), update.getId());
        }
        return flag;
    }

    private void compareUpdateData(List<Typhoidtheoryprescriptionworkcited> newList, long id) {
        if (newList.size() > 0) {
            List<Typhoidtheoryprescriptionworkcited> oldList = workcitedService.queryListByPreparationId(id);
            for (int i = 0; i < newList.size(); i++) {
                Typhoidtheoryprescriptionworkcited newData = newList.get(i);
                if (newData.getId() == null &&
                    (StringUtils.isNotBlank(newData.getContent()) ||
                        StringUtils.isNotBlank(newData.getProvenance()))) {
                    newData.setPreparationId(id);
                    workcitedService.insert(newData);
                } else if (newData.getId() != null) {
                    for (int j = 0; j < oldList.size(); j++) {
                        Typhoidtheoryprescriptionworkcited oldData = oldList.get(j);
                        if (newData.getId().equals(oldData.getId())) {
                            workcitedService.update(newData);
                            oldList.remove(oldData);
                        }
                    }
                }
            }
            if (oldList.size() > 0) {
                Collection<Long> ids = new ArrayList<>();
                for (Typhoidtheoryprescriptionworkcited oldData : oldList) {
                    ids.add(oldData.getId());
                }
                workcitedService.deleteWithValidByIds(ids, true);
            }
        }
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(Typhoidtheoryprescription entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除经方
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            for (Long id: ids) {
                workcitedService.deleteByPreparationId(id);
            }
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
