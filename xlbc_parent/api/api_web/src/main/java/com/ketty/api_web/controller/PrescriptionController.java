package com.ketty.api_web.controller;

import com.ketty.api_entity.FlowCategory;
import com.ketty.api_entity.Prescription;
import com.ketty.common_utils.R;
import com.ketty.service.PrescriptionService;
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
@Api(tags = "方剂名称")
@RestController
@RequestMapping("/prescription")
public class PrescriptionController {
    @Autowired
    PrescriptionService service;

    @GetMapping("/getAllCommon")
    public R getAllCommon() {
        List<FlowCategory> list = service.getAllCommon();
        return R.ok().data("result",list);
    }

    @GetMapping("/getAllClassic")
    public R getAllClassic() {
        List<Prescription> list = service.getAllClassic();
        return R.ok().data("result",list);
    }

    @PostMapping("/getByName")
    public R getByName(String name) {
        try {
            Prescription prescription = service.getByName(name);
            return R.ok().data("result",prescription);
        } catch (Exception e) {
            return R.error().message(e.getMessage());
        }
    }

    @PostMapping("/selectLikeName")
    public R selectLikeName(String name) {
        try {
            List<Prescription> list = service.selectLikeName(name);
            return R.ok().data("result",list);
        } catch (Exception e) {
            return R.error().message(e.getMessage());
        }
    }

}
