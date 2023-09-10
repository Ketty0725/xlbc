package com.xlbc.app.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.xlbc.common.core.page.TableDataInfo;
import com.xlbc.common.core.domain.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xlbc.common.utils.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.xlbc.app.domain.Communityimage;
import com.xlbc.app.mapper.CommunityimageMapper;
import com.xlbc.app.service.ICommunityimageService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 笔记图片Service业务层处理
 *
 * @author ketty
 * @date 2023-04-09
 */
@RequiredArgsConstructor
@Service
@DS("slave")
public class CommunityimageServiceImpl implements ICommunityimageService {

    private final CommunityimageMapper baseMapper;

    /**
     * 查询笔记图片
     */
    @Override
    public Communityimage queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询笔记图片列表
     */
    @Override
    public List<Communityimage> queryListByNoteId(Long noteId) {
        LambdaQueryWrapper<Communityimage> lqw = Wrappers.lambdaQuery();
        lqw.eq(noteId != null, Communityimage::getNoteId, noteId);
        return baseMapper.selectVoList(lqw);
    }

    /**
     * 批量删除笔记图片
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
