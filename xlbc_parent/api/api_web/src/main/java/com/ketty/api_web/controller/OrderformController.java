package com.ketty.api_web.controller;

import com.ketty.api_entity.Orderform;
import com.ketty.api_entity.Product;
import com.ketty.api_entity.Shoppingaddress;
import com.ketty.common_utils.R;
import com.ketty.service.OrderformService;
import com.ketty.service.ProductService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author ketty
 * @since 2023-02-23
 */
@Api(tags = "订单信息")
@RestController
@RequestMapping("/orderform")
public class OrderformController {
    @Autowired
    OrderformService service;
    @Autowired
    ProductService productService;

    @PostMapping("/add")
    public R add(@RequestBody List<Orderform> list) {
        if (list.size() > 0) {
            service.add(list);
            return R.ok();
        }
        return R.error();
    }

    @PostMapping("/getListByState")
    public R getListByState(Long userId, Integer state) {
        if (userId != null && state != null) {
            List<Orderform> list = service.getListByState(userId,state);
            return R.ok().data("result",list);
        }
        return R.error();
    }

    @PostMapping("/getById")
    public R getById(Long id) {
        if (id != null) {
            Orderform bean = service.getById(id);
            return R.ok().data("result",bean);
        }
        return R.error();
    }

    @PostMapping("/alterState")
    public R alterState(Long id, Integer state) {
        if (id != null && state != null) {
            if (state.equals(-1)) {
                service.delete(id);
            } else {
                service.alterState(id,state);
            }
            return R.ok();
        }
        return R.error();
    }

    @PostMapping("/delete")
    public R delete(Long id) {
        if (id != null) {
            service.delete(id);
            return R.ok();
        }
        return R.error();
    }

}
