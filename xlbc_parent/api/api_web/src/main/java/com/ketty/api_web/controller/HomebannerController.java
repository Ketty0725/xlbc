package com.ketty.api_web.controller;

import com.ketty.common_utils.R;
import com.ketty.service.HomebannerService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author ketty
 * @since 2023-04-04
 */
@Api(tags = "首页轮播图")
@RestController
@RequestMapping("/homebanner")
public class HomebannerController {
    @Autowired
    HomebannerService service;

    @GetMapping("/get")
    public R get() {
        List<String> list = service.get();
        return R.ok().data("result", list);
    }
}
