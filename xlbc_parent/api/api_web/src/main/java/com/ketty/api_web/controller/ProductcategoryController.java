package com.ketty.api_web.controller;

import com.ketty.api_entity.Productcategory;
import com.ketty.common_utils.R;
import com.ketty.service.ProductcategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
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
 * @since 2023-04-05
 */
@RestController
@RequestMapping("/productcategory")
public class ProductcategoryController {
    @Autowired
    ProductcategoryService service;

    @GetMapping("/getListByFather")
    public R getListByFather() {
        List<Productcategory> list = service.getListByFather();
        return R.ok().data("result",list);
    }

    @PostMapping("/getListByChild")
    public R getListByChild(Long parentId) {
        List<Productcategory> list = service.getListByChild(parentId);
        return R.ok().data("result",list);
    }

}
