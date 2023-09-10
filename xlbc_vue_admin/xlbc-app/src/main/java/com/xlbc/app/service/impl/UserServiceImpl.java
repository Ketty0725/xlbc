package com.xlbc.app.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.xlbc.common.core.page.TableDataInfo;
import com.xlbc.common.core.domain.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xlbc.common.utils.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.xlbc.app.domain.User;
import com.xlbc.app.mapper.UserMapper;
import com.xlbc.app.service.IUserService;

import java.util.List;
import java.util.Collection;

/**
 * 用户Service业务层处理
 *
 * @author ketty
 * @date 2023-03-18
 */
@RequiredArgsConstructor
@Service
@DS("slave")
public class UserServiceImpl implements IUserService {

    private final UserMapper baseMapper;

    /**
     * 查询用户
     */
    @Override
    public User queryById(Long uId){
        return baseMapper.selectVoById(uId);
    }

    @Override
    public List<User> queryByName(String uName) {
        return baseMapper.selectList(new LambdaQueryWrapper<User>().like(User::getUName,uName));
    }

    /**
     * 查询用户列表
     */
    @Override
    public TableDataInfo<User> queryPageList(User bean, PageQuery pageQuery) {
        LambdaQueryWrapper<User> lqw = buildQueryWrapper(bean);
        Page<User> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询用户列表
     */
    @Override
    public List<User> queryList(User bean) {
        LambdaQueryWrapper<User> lqw = buildQueryWrapper(bean);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<User> buildQueryWrapper(User bean) {
        LambdaQueryWrapper<User> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.isNotBlank(bean.getUPhone()), User::getUPhone, bean.getUPhone());
        lqw.like(StringUtils.isNotBlank(bean.getUName()), User::getUName, bean.getUName());
        lqw.eq(StringUtils.isNotBlank(bean.getUSex()), User::getUSex, bean.getUSex());
        lqw.like(StringUtils.isNotBlank(bean.getUAddress()), User::getUAddress, bean.getUAddress());
        lqw.like(StringUtils.isNotBlank(bean.getIpAddress()), User::getIpAddress, bean.getIpAddress());
        return lqw;
    }

    /**
     * 批量删除用户
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
