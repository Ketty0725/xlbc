package com.ketty.api_web.controller;

import com.ketty.api_entity.Typhoidtheoryprescription;
import com.ketty.common_utils.R;
import com.ketty.service.TyphoidtheoryprescriptionService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
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
@Api(tags = "伤寒论经方")
@RestController
@RequestMapping("/typhoidtheoryprescription")
public class TyphoidtheoryprescriptionController {
    @Autowired
    TyphoidtheoryprescriptionService service;

    @PostMapping("/selectNameList")
    public R selectNameList(String typeName) {
        List<String> list = service.selectNameList(typeName);
        if (list != null) {
            return R.ok().data("result",list);
        }
        return R.error();
    }

    @PostMapping("/getByName")
    public R getByName(String name) {
        Typhoidtheoryprescription entity = service.getByName(name);
        if (entity != null) {
            return R.ok().data("result",entity);
        }
        return R.error();
    }

}
