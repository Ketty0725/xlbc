package com.xlbc.web.controller.app;

import java.util.List;
import java.util.Arrays;

import com.xlbc.app.domain.Chineseherbcategory;
import com.xlbc.app.domain.Prescriptioncategory;
import lombok.RequiredArgsConstructor;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.*;
import cn.dev33.satoken.annotation.SaCheckPermission;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;
import com.xlbc.common.annotation.RepeatSubmit;
import com.xlbc.common.annotation.Log;
import com.xlbc.common.core.controller.BaseController;
import com.xlbc.common.core.domain.R;
import com.xlbc.common.core.validate.AddGroup;
import com.xlbc.common.core.validate.EditGroup;
import com.xlbc.common.enums.BusinessType;
import com.xlbc.common.utils.poi.ExcelUtil;
import com.xlbc.app.service.IPrescriptioncategoryService;

/**
 * 方剂分类
 *
 * @author ketty
 * @date 2023-04-01
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/app/prescriptioncategory")
public class PrescriptioncategoryController extends BaseController {

    private final IPrescriptioncategoryService iPrescriptioncategoryService;

    /**
     * 查询方剂分类列表
     */
    @SaCheckPermission("app:prescriptioncategory:list")
    @GetMapping("/list")
    public R<List<Prescriptioncategory>> list(Prescriptioncategory bo) {
        List<Prescriptioncategory> list = iPrescriptioncategoryService.queryList(bo);
        return R.ok(list);
    }

    @SaCheckPermission("app:prescriptioncategory:list")
    @GetMapping("/listByParent/{parentId}")
    public R<List<Prescriptioncategory>> listByParent(@NotNull(message = "主键不能为空")
                                                     @PathVariable Long parentId) {
        List<Prescriptioncategory> list = iPrescriptioncategoryService.queryListByParent(parentId);
        return R.ok(list);
    }

    /**
     * 导出方剂分类列表
     */
    @SaCheckPermission("app:prescriptioncategory:export")
    @Log(title = "方剂分类", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(Prescriptioncategory bo, HttpServletResponse response) {
        List<Prescriptioncategory> list = iPrescriptioncategoryService.queryList(bo);
        ExcelUtil.exportExcel(list, "方剂分类", Prescriptioncategory.class, response);
    }

    /**
     * 获取方剂分类详细信息
     *
     * @param categoryId 主键
     */
    @SaCheckPermission("app:prescriptioncategory:query")
    @GetMapping("/{categoryId}")
    public R<Prescriptioncategory> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long categoryId) {
        return R.ok(iPrescriptioncategoryService.queryById(categoryId));
    }

    /**
     * 新增方剂分类
     */
    @SaCheckPermission("app:prescriptioncategory:add")
    @Log(title = "方剂分类", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody Prescriptioncategory bo) {
        return toAjax(iPrescriptioncategoryService.insert(bo));
    }

    /**
     * 修改方剂分类
     */
    @SaCheckPermission("app:prescriptioncategory:edit")
    @Log(title = "方剂分类", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody Prescriptioncategory bo) {
        return toAjax(iPrescriptioncategoryService.update(bo));
    }

    /**
     * 删除方剂分类
     *
     * @param categoryIds 主键串
     */
    @SaCheckPermission("app:prescriptioncategory:remove")
    @Log(title = "方剂分类", businessType = BusinessType.DELETE)
    @DeleteMapping("/{categoryIds}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] categoryIds) {
        return toAjax(iPrescriptioncategoryService.deleteWithValidByIds(Arrays.asList(categoryIds), true));
    }
}
