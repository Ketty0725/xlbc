package com.ketty.service;

import com.ketty.api_entity.Typhoidtheoryprescriptionworkcited;
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
public interface TyphoidtheoryprescriptionworkcitedService extends IService<Typhoidtheoryprescriptionworkcited> {
    List<Typhoidtheoryprescriptionworkcited> getByNameAndType(String name, String typeName);

    Long countByName(String name);
}
