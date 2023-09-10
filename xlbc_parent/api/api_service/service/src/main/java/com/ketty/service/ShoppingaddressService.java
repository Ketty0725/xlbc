package com.ketty.service;

import com.ketty.api_entity.Shoppingaddress;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ketty
 * @since 2023-02-25
 */
public interface ShoppingaddressService extends IService<Shoppingaddress> {

    void add(Shoppingaddress bean);

    List<Shoppingaddress> getByUserId(Long userId);

    Shoppingaddress getDefaultByUserId(Long userId);

    void updateIsDefault(Integer id, Integer idDefault);

    void updateData(Shoppingaddress bean);

    void deleteByUser(Long userId);

}
