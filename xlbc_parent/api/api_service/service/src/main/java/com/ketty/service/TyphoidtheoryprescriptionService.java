package com.ketty.service;

import com.ketty.api_entity.Typhoidtheoryprescription;
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
public interface TyphoidtheoryprescriptionService extends IService<Typhoidtheoryprescription> {
    List<String> selectNameList(String typeName);

    Typhoidtheoryprescription getByName(String name);

}
