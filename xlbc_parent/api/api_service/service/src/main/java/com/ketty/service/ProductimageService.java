package com.ketty.service;

import com.ketty.api_entity.Productimage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ketty
 * @since 2023-02-17
 */
public interface ProductimageService extends IService<Productimage> {

    List<String> listByProductId(Long productId);
}
