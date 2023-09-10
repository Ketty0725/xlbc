package com.ketty.service;

import com.ketty.api_entity.NameAndType;
import com.ketty.api_entity.Typhoidtheorytype;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.HashMap;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ketty
 * @since 2023-01-15
 */
public interface TyphoidtheorytypeService extends IService<Typhoidtheorytype> {

    List<NameAndType> selectLikeName(String name);
}
