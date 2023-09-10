package com.xlbc.web.controller.app;

import java.util.List;
import java.util.Arrays;

import com.xlbc.app.domain.Chineseherbcategory;
import com.xlbc.app.domain.Productcategory;
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
import com.xlbc.app.service.IProductcategoryService;

/**
 * 药品分类
 *
 * @author ketty
 * @date 2023-03-26
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/app/productcategory")
public class ProductcategoryController extends BaseController {

    private final IProductcategoryService iProductcategoryService;

    /**
     * 查询药品分类列表
     */
    @SaCheckPermission("app:productcategory:list")
    @GetMapping("/list")
    public R<List<Productcategory>> list(Productcategory bo) {
        List<Productcategory> list = iProductcategoryService.queryList(bo);
        return R.ok(list);
    }

    @SaCheckPermission("app:productcategory:list")
    @GetMapping("/listByParent/{parentId}")
    public R<List<Productcategory>> listByParent(@NotNull(message = "主键不能为空")
                                                     @PathVariable Long parentId) {
        List<Productcategory> list = iProductcategoryService.queryListByParent(parentId);
        return R.ok(list);
    }

    /**
     * 导出药品分类列表
     */
    @SaCheckPermission("app:productcategory:export")
    @Log(title = "药品分类", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(Productcategory bo, HttpServletResponse response) {
        List<Productcategory> list = iProductcategoryService.queryList(bo);
        ExcelUtil.exportExcel(list, "药品分类", Productcategory.class, response);
    }

    /**
     * 获取药品分类详细信息
     *
     * @param categoryId 主键
     */
    @SaCheckPermission("app:productcategory:query")
    @GetMapping("/{categoryId}")
    public R<Productcategory> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long categoryId) {
        return R.ok(iProductcategoryService.queryById(categoryId));
    }

    /**
     * 新增药品分类
     */
    @SaCheckPermission("app:productcategory:add")
    @Log(title = "药品分类", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody Productcategory bo) {
        return toAjax(iProductcategoryService.insert(bo));
    }

    /**
     * 修改药品分类
     */
    @SaCheckPermission("app:productcategory:edit")
    @Log(title = "药品分类", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody Productcategory bo) {
        return toAjax(iProductcategoryService.update(bo));
    }

    /**
     * 删除药品分类
     *
     * @param categoryIds 主键串
     */
    @SaCheckPermission("app:productcategory:remove")
    @Log(title = "药品分类", businessType = BusinessType.DELETE)
    @DeleteMapping("/{categoryIds}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] categoryIds) {
        return toAjax(iProductcategoryService.deleteWithValidByIds(Arrays.asList(categoryIds), true));
    }
}
