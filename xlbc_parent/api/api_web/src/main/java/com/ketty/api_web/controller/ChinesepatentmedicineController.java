package com.ketty.api_web.controller;

import com.ketty.api_entity.Chinesepatentmedicine;
import com.ketty.common_utils.R;
import com.ketty.service.ChinesepatentmedicineService;
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
@Api(tags = "中成药")
@RestController
@RequestMapping("/chinesepatentmedicine")
public class ChinesepatentmedicineController {
    @Autowired
    ChinesepatentmedicineService service;

    @PostMapping("/add")
    public R add(Chinesepatentmedicine chinesepatentmedicine) {
        boolean isAdd = service.save(chinesepatentmedicine);
        if (isAdd) {
            return R.ok();
        } else {
            return R.error();
        }
    }

    @GetMapping("/getNameList")
    public R getNameList() {
        List<String> list = service.getNameList();
        return R.ok().data("result", list);
    }

    @PostMapping("/getByName")
    public R getByName(String name) {
        Chinesepatentmedicine chinesepatentmedicine = service.getByName(name);
        return R.ok().data("result", chinesepatentmedicine);
    }

    @PostMapping("/selectLikeName")
    public R selectLikeName(String name) {
        try {
            List<String> list = service.selectLikeName(name);
            return R.ok().data("result",list);
        } catch (Exception e) {
            return R.error().message(e.getMessage());
        }
    }

}
