package com.ketty.api_web.controller;

import com.ketty.api_entity.Productdoctorqa;
import com.ketty.common_utils.R;
import com.ketty.service.ProductdoctorqaService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author ketty
 * @since 2023-02-19
 */
@Api(tags = "商品-医生问答")
@RestController
@RequestMapping("/productdoctorqa")
public class ProductdoctorqaController {
    @Autowired
    ProductdoctorqaService service;

    @PostMapping("/add")
    public R add(Productdoctorqa bean) {
        if (bean != null) {
            service.save(bean);
            return R.ok();
        }
        return R.error();
    }

    @PostMapping("/listByProductId")
    public R listByProductId(Long productId) {
        if (productId != null) {
            List<Productdoctorqa> list = service.listByProductId(productId);
            return R.ok().data("result",list);
        }
        return R.error();
    }

    @PostMapping("/countByProductId")
    public R countByProductId(Long productId) {
        if (productId != null) {
            Long count = service.countByProductId(productId);
            return R.ok().data("result",count);
        }
        return R.error();
    }

    @PostMapping("/getFirstTwo")
    public R getFirstTwo(Long productId) {
        if (productId != null) {
            List<Productdoctorqa> list = service.getFirstTwo(productId);
            return R.ok().data("result",list);
        }
        return R.error();
    }

}
