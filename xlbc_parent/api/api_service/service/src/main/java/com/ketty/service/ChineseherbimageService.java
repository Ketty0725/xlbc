package com.ketty.service;

import com.ketty.api_entity.Chineseherbimage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ketty
 * @since 2023-01-15
 */
public interface ChineseherbimageService extends IService<Chineseherbimage> {
    List<String> queryByCbId(Long cbId);
}
