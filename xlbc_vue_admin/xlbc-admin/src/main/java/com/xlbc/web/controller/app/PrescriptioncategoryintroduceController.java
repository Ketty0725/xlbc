package com.xlbc.web.controller.app;

import java.util.List;
import java.util.Arrays;

import com.xlbc.app.domain.Prescriptioncategoryintroduce;
import lombok.RequiredArgsConstructor;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.*;
import cn.dev33.satoken.annotation.SaCheckPermission;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;
import com.xlbc.common.annotation.RepeatSubmit;
import com.xlbc.common.annotation.Log;
import com.xlbc.common.core.controller.BaseController;
import com.xlbc.common.core.domain.PageQuery;
import com.xlbc.common.core.domain.R;
import com.xlbc.common.core.validate.AddGroup;
import com.xlbc.common.core.validate.EditGroup;
import com.xlbc.common.enums.BusinessType;
import com.xlbc.common.utils.poi.ExcelUtil;
import com.xlbc.app.service.IPrescriptioncategoryintroduceService;
import com.xlbc.common.core.page.TableDataInfo;

/**
 * 分类介绍
 *
 * @author ketty
 * @date 2023-04-01
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/app/prescriptioncategoryintroduce")
public class PrescriptioncategoryintroduceController extends BaseController {

    private final IPrescriptioncategoryintroduceService iPrescriptioncategoryintroduceService;

    /**
     * 查询分类介绍列表
     */
    @SaCheckPermission("app:prescriptioncategoryintroduce:list")
    @GetMapping("/list")
    public TableDataInfo<Prescriptioncategoryintroduce> list(Prescriptioncategoryintroduce bo, PageQuery pageQuery) {
        return iPrescriptioncategoryintroduceService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出分类介绍列表
     */
    @SaCheckPermission("app:prescriptioncategoryintroduce:export")
    @Log(title = "分类介绍", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(Prescriptioncategoryintroduce bo, HttpServletResponse response) {
        List<Prescriptioncategoryintroduce> list = iPrescriptioncategoryintroduceService.queryList(bo);
        ExcelUtil.exportExcel(list, "分类介绍", Prescriptioncategoryintroduce.class, response);
    }

    /**
     * 获取分类介绍详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("app:prescriptioncategoryintroduce:query")
    @GetMapping("/{id}")
    public R<Prescriptioncategoryintroduce> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(iPrescriptioncategoryintroduceService.queryById(id));
    }

    /**
     * 新增分类介绍
     */
    @SaCheckPermission("app:prescriptioncategoryintroduce:add")
    @Log(title = "分类介绍", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody Prescriptioncategoryintroduce bo) {
        return toAjax(iPrescriptioncategoryintroduceService.insert(bo));
    }

    /**
     * 修改分类介绍
     */
    @SaCheckPermission("app:prescriptioncategoryintroduce:edit")
    @Log(title = "分类介绍", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody Prescriptioncategoryintroduce bo) {
        return toAjax(iPrescriptioncategoryintroduceService.update(bo));
    }

    /**
     * 删除分类介绍
     *
     * @param ids 主键串
     */
    @SaCheckPermission("app:prescriptioncategoryintroduce:remove")
    @Log(title = "分类介绍", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(iPrescriptioncategoryintroduceService.deleteWithValidByIds(Arrays.asList(ids), true));
    }
}
