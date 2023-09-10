package com.xlbc.app.service;

import com.xlbc.app.domain.User;
import com.xlbc.common.core.page.TableDataInfo;
import com.xlbc.common.core.domain.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 用户Service接口
 *
 * @author ketty
 * @date 2023-03-18
 */
public interface IUserService {

    /**
     * 查询用户
     */
    User queryById(Long uId);

    List<User> queryByName(String uName);

    /**
     * 查询用户列表
     */
    TableDataInfo<User> queryPageList(User bean, PageQuery pageQuery);

    /**
     * 查询用户列表
     */
    List<User> queryList(User bean);

    /**
     * 校验并批量删除用户信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
