package com.ketty.service;

import com.ketty.api_entity.Chineseherbcategoryintroduce;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ketty
 * @since 2023-04-04
 */
public interface ChineseherbcategoryintroduceService extends IService<Chineseherbcategoryintroduce> {

    Chineseherbcategoryintroduce getByTitle(String title);
}
