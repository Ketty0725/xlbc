package com.xlbc.app.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.xlbc.app.domain.Typhoidtheorychineseherbcontent;
import com.xlbc.app.domain.Typhoidtheoryprescription;
import com.xlbc.app.domain.Typhoidtheoryprescriptionworkcited;
import com.xlbc.app.service.ITyphoidtheorychineseherbcontentService;
import com.xlbc.common.core.page.TableDataInfo;
import com.xlbc.common.core.domain.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xlbc.common.utils.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.xlbc.app.domain.Typhoidtheorychineseherb;
import com.xlbc.app.mapper.TyphoidtheorychineseherbMapper;
import com.xlbc.app.service.ITyphoidtheorychineseherbService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 中药Service业务层处理
 *
 * @author ketty
 * @date 2023-04-02
 */
@RequiredArgsConstructor
@Service
@DS("slave")
public class TyphoidtheorychineseherbServiceImpl implements ITyphoidtheorychineseherbService {

    private final TyphoidtheorychineseherbMapper baseMapper;

    private final ITyphoidtheorychineseherbcontentService contentService;

    /**
     * 查询中药
     */
    @Override
    public Typhoidtheorychineseherb queryById(Long id){
        Typhoidtheorychineseherb bean = baseMapper.selectVoById(id);
        String abstractSql = "where (cb_id = " + id + " AND type_id = 4)";
        List<Typhoidtheorychineseherbcontent> abstracts = contentService.queryList(abstractSql);
        bean.setAbstracts(abstracts);
        String citeSql = "where (cb_id = " + id + " AND type_id != 4)";
        List<Typhoidtheorychineseherbcontent> cites = contentService.queryList(citeSql);
        bean.setCites(cites);
        return bean;
    }

    /**
     * 查询中药列表
     */
    @Override
    public TableDataInfo<Typhoidtheorychineseherb> queryPageList(Typhoidtheorychineseherb bo, PageQuery pageQuery) {
        LambdaQueryWrapper<Typhoidtheorychineseherb> lqw = buildQueryWrapper(bo);
        Page<Typhoidtheorychineseherb> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        for (Typhoidtheorychineseherb bean : result.getRecords()) {
            String abstractSql = "where (cb_id = " + bean.getId() + " AND type_id = 4) limit 1";
            List<Typhoidtheorychineseherbcontent> abstracts = contentService.queryList(abstractSql);
            bean.setAbstracts(abstracts);
            String citeSql = "where (cb_id = " + bean.getId() + " AND type_id != 4) limit 1";
            List<Typhoidtheorychineseherbcontent> cites = contentService.queryList(citeSql);
            bean.setCites(cites);
        }
        return TableDataInfo.build(result);
    }

    /**
     * 查询中药列表
     */
    @Override
    public List<Typhoidtheorychineseherb> queryList(Typhoidtheorychineseherb bo) {
        LambdaQueryWrapper<Typhoidtheorychineseherb> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<Typhoidtheorychineseherb> buildQueryWrapper(Typhoidtheorychineseherb bo) {
        LambdaQueryWrapper<Typhoidtheorychineseherb> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.isNotBlank(bo.getName()), Typhoidtheorychineseherb::getName, bo.getName());
        return lqw;
    }

    /**
     * 新增中药
     */
    @Override
    public Boolean insert(Typhoidtheorychineseherb bo) {
        Typhoidtheorychineseherb add = BeanUtil.toBean(bo, Typhoidtheorychineseherb.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
            if (add.getAbstracts().size() > 0) {
                for (Typhoidtheorychineseherbcontent bean : add.getAbstracts()) {
                    if (StringUtils.isNotBlank(bean.getContent())) {
                        bean.setCbId(bo.getId());
                        bean.setTypeId(4L);
                        contentService.insert(bean);
                    }
                }
            }
            if (add.getCites().size() > 0) {
                for (Typhoidtheorychineseherbcontent bean : add.getCites()) {
                    if (StringUtils.isNotBlank(bean.getContent())) {
                        bean.setCbId(bo.getId());
                        contentService.insert(bean);
                    }
                }
            }
        }
        return flag;
    }

    /**
     * 修改中药
     */
    @Override
    public Boolean update(Typhoidtheorychineseherb bo) {
        Typhoidtheorychineseherb update = BeanUtil.toBean(bo, Typhoidtheorychineseherb.class);
        validEntityBeforeSave(update);
        boolean flag = baseMapper.updateById(update) > 0;
        if (flag) {
            long id = update.getId();
            String abstractSql = "where (cb_id = " + id + " AND type_id = 4)";
            compareUpdateData(update.getAbstracts(), id, abstractSql);

            String citeSql = "where (cb_id = " + id + " AND type_id != 4)";
            compareUpdateData(update.getCites(), id, citeSql);
        }
        return flag;
    }

    private void compareUpdateData(List<Typhoidtheorychineseherbcontent> newList, long id, String sql) {
        if (newList.size() > 0) {
            List<Typhoidtheorychineseherbcontent> oldList = contentService.queryList(sql);
            for (int i = 0; i < newList.size(); i++) {
                Typhoidtheorychineseherbcontent newData = newList.get(i);
                if (newData.getId() == null && StringUtils.isNotBlank(newData.getContent())) {
                    newData.setCbId(id);
                    if (newData.getTypeId() == null) {
                        newData.setTypeId(4L);
                    }
                    contentService.insert(newData);
                } else if (newData.getId() != null) {
                    for (int j = 0; j < oldList.size(); j++) {
                        Typhoidtheorychineseherbcontent oldData = oldList.get(j);
                        if (newData.getId().equals(oldData.getId())) {
                            contentService.update(newData);
                            oldList.remove(oldData);
                        }
                    }
                }
            }
            if (oldList.size() > 0) {
                for (Typhoidtheorychineseherbcontent oldData : oldList) {
                    contentService.deleteById(oldData.getId());
                }
            }
        }
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(Typhoidtheorychineseherb entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除中药
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            for (Long id: ids) {
                contentService.deleteByCbId(id);
            }
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
