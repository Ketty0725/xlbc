package com.ketty.api_web.controller;

import com.ketty.api_entity.Chineseherbcategoryintroduce;
import com.ketty.common_utils.R;
import com.ketty.service.ChineseherbcategoryintroduceService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author ketty
 * @since 2023-04-04
 */
@Api(tags = "中药分类介绍")
@RestController
@RequestMapping("/chineseherbcategoryintroduce")
public class ChineseherbcategoryintroduceController {
    @Autowired
    ChineseherbcategoryintroduceService service;

    @PostMapping("/getByTitle")
    public R getByTitle(String title) {
        Chineseherbcategoryintroduce bean = service.getByTitle(title);
        if (bean != null) {
            return R.ok().data("result",bean);
        } else {
            return R.error();
        }
    }

}
