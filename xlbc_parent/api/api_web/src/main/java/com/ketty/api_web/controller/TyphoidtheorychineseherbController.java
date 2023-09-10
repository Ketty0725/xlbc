package com.ketty.api_web.controller;

import com.ketty.api_entity.Typhoidtheorychineseherb;
import com.ketty.common_utils.R;
import com.ketty.service.TyphoidtheorychineseherbService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author ketty
 * @since 2023-01-16
 */
@Api(tags = "伤寒论中药")
@RestController
@RequestMapping("/typhoidtheorychineseherb")
public class TyphoidtheorychineseherbController {
    @Autowired
    TyphoidtheorychineseherbService service;

    @PostMapping("/getAll")
    public R getAll() {
        List<String> list = new ArrayList<>();
        for (Typhoidtheorychineseherb entity : service.list()) {
            list.add(entity.getName());
        }
        return R.ok().data("result",list);
    }

}
