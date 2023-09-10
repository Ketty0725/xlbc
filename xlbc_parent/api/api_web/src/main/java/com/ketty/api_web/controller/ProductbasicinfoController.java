package com.ketty.api_web.controller;

import com.ketty.api_entity.Productbasicinfo;
import com.ketty.api_entity.Productimage;
import com.ketty.common_utils.R;
import com.ketty.service.ProductbasicinfoService;
import com.ketty.service.ProductimageService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
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
 * @since 2023-02-17
 */
@Api(tags = "商品基本信息")
@RestController
@RequestMapping("/productbasicinfo")
public class ProductbasicinfoController {
    @Autowired
    ProductbasicinfoService basicinfoService;
    @Autowired
    ProductimageService imageService;

    @PostMapping("/getByProductId")
    public R getByProductId(Long productId) {
        if (productId != null) {
            Productbasicinfo productbasicinfo = basicinfoService.getByProductId(productId);
            List<String> images = imageService.listByProductId(productId);
            return R.ok().data("basicinfo",productbasicinfo).data("images",images);
        }
        return R.error();
    }

    @PostMapping("/getInstructionBook")
    public R getInstructionBook(Long productId) {
        if (productId != null) {
            String info = basicinfoService.getInstructionBook(productId);
            return R.ok().data("result",info);
        }
        return R.error();
    }

}
