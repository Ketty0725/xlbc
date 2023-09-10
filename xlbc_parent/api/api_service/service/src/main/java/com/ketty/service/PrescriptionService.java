package com.ketty.service;

import com.ketty.api_entity.FlowCategory;
import com.ketty.api_entity.Prescription;
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
public interface PrescriptionService extends IService<Prescription> {

    List<FlowCategory> getAllCommon();

    Prescription getByName(String name);

    List<Prescription> selectLikeName(String name);

    List<Prescription> getAllClassic();
}
