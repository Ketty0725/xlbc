package com.ketty.serviceimpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ketty.api_entity.Productdoctorqa;
import com.ketty.api_mapper.ProductdoctorqaMapper;
import com.ketty.service.ProductdoctorqaService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ketty
 * @since 2023-02-19
 */
@Service
public class ProductdoctorqaServiceImpl extends ServiceImpl<ProductdoctorqaMapper, Productdoctorqa> implements ProductdoctorqaService {
    @Autowired
    ProductdoctorqaMapper mapper;

    @Override
    public List<Productdoctorqa> listByProductId(Long productId) {
        List<Productdoctorqa> list = mapper.selectList(
                new LambdaQueryWrapper<Productdoctorqa>()
                        .eq(Productdoctorqa::getProductId,productId));
        return list;
    }

    @Override
    public Long countByProductId(Long productId) {
        Long num = mapper.selectCount(
                new LambdaQueryWrapper<Productdoctorqa>()
                        .eq(Productdoctorqa::getProductId,productId));
        return num;
    }

    @Override
    public List<Productdoctorqa> getFirstTwo(Long productId) {
        List<Productdoctorqa> list = mapper.selectList(
                new LambdaQueryWrapper<Productdoctorqa>()
                        .eq(Productdoctorqa::getProductId,productId)
                        .last("limit 2"));
        return list;
    }

}
