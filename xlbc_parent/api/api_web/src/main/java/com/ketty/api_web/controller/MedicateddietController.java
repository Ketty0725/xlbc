package com.ketty.api_web.controller;

import com.ketty.api_entity.Medicateddiet;
import com.ketty.common_utils.R;
import com.ketty.service.MedicateddietService;
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
@Api(tags = "药膳")
@RestController
@RequestMapping("/medicateddiet")
public class MedicateddietController {
    @Autowired
    MedicateddietService service;

    @PostMapping("/add")
    public R add(Medicateddiet medicateddiet) {
        boolean isAdd = service.save(medicateddiet);
        if (isAdd) {
            return R.ok();
        } else {
            return R.error();
        }
    }

    @GetMapping("/getNameList")
    public R getNameList() {
        List<Medicateddiet> list = service.getNameList();
        return R.ok().data("result", list);
    }

    @PostMapping("/getByName")
    public R getByName(String name) {
        Medicateddiet medicateddiet = service.getByName(name);
        return R.ok().data("result", medicateddiet);
    }

    @PostMapping("/selectLikeName")
    public R selectLikeName(String name) {
        try {
            List<Medicateddiet> list = service.selectLikeName(name);
            return R.ok().data("result",list);
        } catch (Exception e) {
            return R.error().message(e.getMessage());
        }
    }

}
