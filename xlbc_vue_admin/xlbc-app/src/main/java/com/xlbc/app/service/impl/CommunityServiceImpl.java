package com.xlbc.app.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.xlbc.app.domain.Communityimage;
import com.xlbc.app.domain.User;
import com.xlbc.app.service.ICommunityimageService;
import com.xlbc.app.service.IUserService;
import com.xlbc.common.core.page.TableDataInfo;
import com.xlbc.common.core.domain.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xlbc.common.core.service.UserService;
import com.xlbc.common.utils.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.xlbc.app.domain.Community;
import com.xlbc.app.mapper.CommunityMapper;
import com.xlbc.app.service.ICommunityService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 笔记Service业务层处理
 *
 * @author ketty
 * @date 2023-03-26
 */
@RequiredArgsConstructor
@Service
@DS("slave")
public class CommunityServiceImpl implements ICommunityService {

    private final CommunityMapper baseMapper;

    private final IUserService userService;

    private final ICommunityimageService imageService;

    /**
     * 查询笔记
     */
    @Override
    public Community queryById(Long id){
        Community community = baseMapper.selectVoById(id);
        User user = userService.queryById(community.getUserId());
        community.setUser(user);
        List<Communityimage> images = imageService.queryListByNoteId(community.getId());
        community.setImages(images);
        return community;
    }

    /**
     * 查询笔记列表
     */
    @Override
    public TableDataInfo<Community> queryPageList(Community bo, String uName, PageQuery pageQuery) {
        LambdaQueryWrapper<Community> lqw = buildQueryWrapper(bo,uName);
        Page<Community> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        for (Community community : result.getRecords()) {
            User user = userService.queryById(community.getUserId());
            community.setUser(user);
            List<Communityimage> images = imageService.queryListByNoteId(community.getId());
            community.setImages(images);
        }
        return TableDataInfo.build(result);
    }

    /**
     * 查询笔记列表
     */
    @Override
    public List<Community> queryList(Community bo) {
        LambdaQueryWrapper<Community> lqw = buildQueryWrapper(bo,null);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<Community> buildQueryWrapper(Community bo, String uName) {
        LambdaQueryWrapper<Community> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.isNotBlank(bo.getTitle()), Community::getTitle, bo.getTitle());
        lqw.like(StringUtils.isNotBlank(bo.getIpAddress()), Community::getIpAddress, bo.getIpAddress());
        if (uName != null) {
            List<User> users = userService.queryByName(uName);
            if (users.size() > 0) {
                for (User user: users) {
                    lqw.eq(user.getUId() != null, Community::getUserId, user.getUId());
                }
            }
        }
        lqw.eq(bo.getDeleted() != null, Community::getDeleted, bo.getDeleted());
        return lqw;
    }

    /**
     * 新增笔记
     */
    @Override
    public Boolean insert(Community bo) {
        Community add = BeanUtil.toBean(bo, Community.class);
        validEntityBeforeSave(add);
        return baseMapper.insert(add) > 0;
    }

    /**
     * 修改笔记
     */
    @Override
    public Boolean update(Community bo) {
        Community update = BeanUtil.toBean(bo, Community.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(Community entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除笔记
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
