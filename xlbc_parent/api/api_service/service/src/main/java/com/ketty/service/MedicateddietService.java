package com.ketty.service;

import com.ketty.api_entity.Medicateddiet;
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
public interface MedicateddietService extends IService<Medicateddiet> {
    List<Medicateddiet> getNameList();

    Medicateddiet getByName(String name);

    List<Medicateddiet> selectLikeName(String name);
}
