package com.ketty.service;

import com.ketty.api_entity.Typhoidtheorychineseherbcontent;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ketty
 * @since 2023-01-16
 */
public interface TyphoidtheorychineseherbcontentService extends IService<Typhoidtheorychineseherbcontent> {

    List<Typhoidtheorychineseherbcontent> getByNameAndType(String name, String typeName);
}
