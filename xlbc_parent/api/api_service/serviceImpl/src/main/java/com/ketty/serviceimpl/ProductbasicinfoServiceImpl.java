package com.ketty.serviceimpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ketty.api_entity.Product;
import com.ketty.api_entity.Productbasicinfo;
import com.ketty.api_entity.Productimage;
import com.ketty.api_mapper.ProductMapper;
import com.ketty.api_mapper.ProductbasicinfoMapper;
import com.ketty.common_base.MinioUtil;
import com.ketty.service.ProductbasicinfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ketty.service.ProductimageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ketty
 * @since 2023-02-17
 */
@Service
public class ProductbasicinfoServiceImpl extends ServiceImpl<ProductbasicinfoMapper, Productbasicinfo> implements ProductbasicinfoService {
    @Autowired
    ProductbasicinfoMapper basicinfoMapper;
    @Autowired
    ProductimageService imageService;
    @Autowired
    ProductMapper productMapper;

    @Override
    public Productbasicinfo getByProductId(Long productId) {
        Productbasicinfo bean = basicinfoMapper.selectOne(
                new LambdaQueryWrapper<Productbasicinfo>()
                        .select(Productbasicinfo.class, info -> !info.getColumn().equals("instruction_book"))
                        .eq(Productbasicinfo::getProductId,productId));
        return bean;
    }

    @Override
    public String getInstructionBook(Long productId) {
        String info = basicinfoMapper.selectOne(
                new LambdaQueryWrapper<Productbasicinfo>()
                        .select(Productbasicinfo::getInstructionBook)
                        .eq(Productbasicinfo::getProductId,productId))
                .getInstructionBook();
        return info;
    }

}
