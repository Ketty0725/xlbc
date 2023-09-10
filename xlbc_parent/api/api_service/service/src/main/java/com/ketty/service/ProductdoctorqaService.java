package com.ketty.service;

import com.ketty.api_entity.Productdoctorqa;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ketty
 * @since 2023-02-19
 */
public interface ProductdoctorqaService extends IService<Productdoctorqa> {

    List<Productdoctorqa> listByProductId(Long productId);

    Long countByProductId(Long productId);

    List<Productdoctorqa> getFirstTwo(Long productId);
}
