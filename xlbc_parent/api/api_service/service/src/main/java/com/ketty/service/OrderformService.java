package com.ketty.service;

import com.ketty.api_entity.Orderform;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ketty
 * @since 2023-02-23
 */
public interface OrderformService extends IService<Orderform> {

    void add(List<Orderform> list);

    List<Orderform> getListByState(Long userId, Integer state);

    void alterState(Long id, Integer state);

    void delete(Long id);

    void deleteByUser(Long userId);
}
