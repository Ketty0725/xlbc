package com.ketty.service;

import com.ketty.api_entity.Homebanner;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ketty
 * @since 2023-04-04
 */
public interface HomebannerService extends IService<Homebanner> {

    List<String> get();
}
