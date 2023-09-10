package com.ketty.api_web.controller;

import com.ketty.api_entity.Orderform;
import com.ketty.api_entity.Product;
import com.ketty.api_entity.Productshopcart;
import com.ketty.common_utils.R;
import com.ketty.service.ProductService;
import com.ketty.service.ProductshopcartService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author ketty
 * @since 2023-02-20
 */
@Api(tags = "购物车")
@RestController
@RequestMapping("/productshopcart")
public class ProductshopcartController {
    @Autowired
    ProductshopcartService service;
    @Autowired
    ProductService productService;

    @PostMapping("/isExistsFromAll")
    public R isExistsFromAll(Long userId, Long productId) {
        boolean isExists = service.isExistsFromAll(userId, productId);
        if (isExists) {
            return R.ok();
        } else {
            return R.error();
        }
    }

    @PostMapping("/addOrIncrementToRedis")
    public R addOrIncrementToRedis(Long userId, Long productId) {
        service.addOrIncrementToRedis(userId, productId);
        return R.ok();
    }

    @PostMapping("/decrementToRedis")
    public R decrementToRedis(Long userId, Long productId) {
        service.decrementToRedis(userId, productId);
        return R.ok();
    }

    @PostMapping("/deleteToRedis")
    public R deleteToRedis(@RequestBody List<Productshopcart> list) {
        if (list.size() > 0) {
            service.deleteToRedis(list);
            return R.ok();
        }
        return R.error();
    }

    @PostMapping("/getByUserId")
    public R getByUserId(Long userId) {
        if (userId != null) {
            List<Productshopcart> list = service.getByUserId(userId);
            List<Product> products = new ArrayList<>();
            if (list != null) {
                for (Productshopcart bean : list) {
                    products.add(productService.getById(bean.getProductId()));
                }
                return R.ok().data("Productshopcart",list).data("Product",products);
            }
        }
        return R.error();
    }

}
