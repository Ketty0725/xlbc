package com.ketty.serviceimpl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ketty.api_entity.Productimage;
import com.ketty.api_entity.SysOss;
import com.ketty.api_mapper.ProductimageMapper;
import com.ketty.common_utils.StringUtils;
import com.ketty.service.ProductimageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ketty.service.SysOssService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
@DS("master")
public class ProductimageServiceImpl extends ServiceImpl<ProductimageMapper, Productimage> implements ProductimageService {
    @Autowired
    ProductimageMapper mapper;
    @Autowired
    SysOssService sysOssService;

    @Override
    public List<String> listByProductId(Long productId) {
        Productimage productimage = mapper.selectOne(
                new LambdaQueryWrapper<Productimage>()
                        .eq(Productimage::getProductId,productId));
        if (productimage != null) {
            String ossIdList = productimage.getOssIdList();
            List<String> images = new ArrayList<>();
            if (StringUtils.isNotBlank(ossIdList)) {
                if (ossIdList.contains(",")) {
                    for (String ossId : StringUtils.splitList(ossIdList)) {
                        SysOss oss = sysOssService.getById(Long.valueOf(ossId));
                        images.add(oss.getUrl());
                    }
                } else {
                    SysOss oss = sysOssService.getById(Long.valueOf(ossIdList));
                    images.add(oss.getUrl());
                }
                return images;
            }
        }
        return null;
    }
}
