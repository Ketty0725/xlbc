package com.ketty.service;

import com.ketty.api_entity.Chinesepatentmedicine;
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
public interface ChinesepatentmedicineService extends IService<Chinesepatentmedicine> {

    List<String> getNameList();

    Chinesepatentmedicine getByName(String name);

    List<String> selectLikeName(String name);
}
