package com.ketty.api_web.controller;

import com.ketty.api_entity.NameAndType;
import com.ketty.api_entity.Typhoidtheorytype;
import com.ketty.common_utils.R;
import com.ketty.service.TyphoidtheorytypeService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author ketty
 * @since 2023-01-15
 */
@Api(tags = "伤寒论查阅类别")
@RestController
@RequestMapping("/typhoidtheorytype")
public class TyphoidtheorytypeController {
    @Autowired
    TyphoidtheorytypeService service;

    @GetMapping("/getAll")
    public R getAll() {
        List<Typhoidtheorytype> list = service.list();
        return R.ok().data("result",list);
    }

    @PostMapping("/selectLikeName")
    public R selectLikeName(String name) {
        try {
            List<NameAndType> list = service.selectLikeName(name);
            return R.ok().data("result",list);
        } catch (Exception e) {
            return R.error().message(e.getMessage());
        }
    }

}
