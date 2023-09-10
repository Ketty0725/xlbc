package com.ketty.api_web.controller;

import com.ketty.api_entity.Collectstatistic;
import com.ketty.api_entity.Product;
import com.ketty.common_utils.R;
import com.ketty.service.ProductService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author ketty
 * @since 2023-02-14
 */
@Api(tags = "商品")
@RestController
@RequestMapping("/product")
public class ProductController {
    @Autowired
    ProductService service;

    @PostMapping("/getById")
    public R getById(Long id) {
        if (id != null) {
            Product product = service.getById(id);
            return R.ok().data("result",product);
        }
        return R.error();
    }

    @PostMapping("/getInventory")
    public R getInventory(Long id) {
        if (id != null) {
            int num = service.getInventory(id);
            return R.ok().data("result",num);
        }
        return R.error();
    }

    @PostMapping("/getByIds")
    public R getByIds(@RequestBody List<Long> ids) {
        if (ids.size() > 0) {
            List<Product> list = service.getByIds(ids);
            return R.ok().data("result",list);
        }
        return R.error();
    }

    @PostMapping("/getItemList")
    public R getItemList(Long categoryId) {
        if (categoryId != null) {
            List<Product> list = service.getItemList(categoryId);
            return R.ok().data("result",list);
        }
        return R.error();
    }

    @PostMapping("/selectLikeName")
    public R selectLikeName(String name) {
        try {
            List<Product> list = service.selectLikeName(name);
            return R.ok().data("result",list);
        } catch (Exception e) {
            return R.error().message(e.getMessage());
        }
    }

    @PostMapping("/updateCollectState")
    public R updateCollectState(Collectstatistic entity) {
        service.updateCollectState(entity);
        return R.ok();
    }

    @PostMapping("/getIsCollectByUserId")
    public R getIsCollectByUserId(Long userId, Long id) {
        int isCollect = service.getIsCollectByUserId(userId, id);
        return R.ok().data("result", isCollect);
    }

    @PostMapping("/getBeCollectList")
    public R getBeCollectList(Long userId) {
        if (userId != null) {
            List<Product> list = service.getBeCollectList(userId);
            return R.ok().data("result", list);
        }
        return R.error();
    }

}
