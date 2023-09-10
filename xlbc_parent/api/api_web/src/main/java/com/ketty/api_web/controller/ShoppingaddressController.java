package com.ketty.api_web.controller;

import com.ketty.api_entity.Shoppingaddress;
import com.ketty.common_utils.R;
import com.ketty.service.ShoppingaddressService;
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
 * @since 2023-02-25
 */
@Api(tags = "收货地址")
@RestController
@RequestMapping("/shoppingaddress")
public class ShoppingaddressController {
    @Autowired
    ShoppingaddressService service;

    @PostMapping("/add")
    public R add(Shoppingaddress bean) {
        if (bean != null) {
            service.add(bean);
            return R.ok();
        }
        return R.error();
    }

    @PostMapping("/getByUserId")
    public R getByUserId(Long userId) {
        if (userId != null) {
            List<Shoppingaddress> list = service.getByUserId(userId);
            return R.ok().data("result",list);
        }
        return R.error();
    }

    @PostMapping("/getDefaultByUserId")
    public R getDefaultByUserId(Long userId) {
        if (userId != null) {
            Shoppingaddress bean = service.getDefaultByUserId(userId);
            return R.ok().data("result",bean);
        }
        return R.error();
    }

    @PostMapping("/getById")
    public R getById(Integer id) {
        if (id != null) {
            Shoppingaddress bean = service.getById(id);
            return R.ok().data("result",bean);
        }
        return R.error();
    }

    @PostMapping("/delete")
    public R delete(Integer id) {
        if (id != null) {
            service.removeById(id);
            return R.ok();
        }
        return R.error();
    }

    @PostMapping("/updateData")
    public R updateData(Shoppingaddress bean) {
        if (bean != null) {
            service.updateData(bean);
            return R.ok();
        }
        return R.error();
    }

}
