package com.xlbc.web.controller.app;

import java.util.List;
import java.util.Arrays;

import com.xlbc.app.domain.Chineseherbcategory;
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
import com.xlbc.app.service.IChineseherbcategoryService;

/**
 * 中药分类
 *
 * @author ketty
 * @date 2023-03-19
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/app/chineseherbcategory")
public class ChineseherbcategoryController extends BaseController {

    private final IChineseherbcategoryService iChineseherbcategoryService;

    /**
     * 查询中药分类列表
     */
    @SaCheckPermission("app:chineseherbcategory:list")
    @GetMapping("/list")
    public R<List<Chineseherbcategory>> list(Chineseherbcategory bean) {
        List<Chineseherbcategory> list = iChineseherbcategoryService.queryList(bean);
        return R.ok(list);
    }

    @SaCheckPermission("app:chineseherbcategory:list")
    @GetMapping("/listByParent/{parentId}")
    public R<List<Chineseherbcategory>> listByParent(@NotNull(message = "主键不能为空")
                                                     @PathVariable Long parentId) {
        List<Chineseherbcategory> list = iChineseherbcategoryService.queryListByParent(parentId);
        return R.ok(list);
    }

    /**
     * 导出中药分类列表
     */
    @SaCheckPermission("app:chineseherbcategory:export")
    @Log(title = "中药分类", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(Chineseherbcategory bean, HttpServletResponse response) {
        List<Chineseherbcategory> list = iChineseherbcategoryService.queryList(bean);
        ExcelUtil.exportExcel(list, "中药分类", Chineseherbcategory.class, response);
    }

    /**
     * 获取中药分类详细信息
     *
     * @param categoryId 主键
     */
    @SaCheckPermission("app:chineseherbcategory:query")
    @GetMapping("/{categoryId}")
    public R<Chineseherbcategory> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long categoryId) {
        return R.ok(iChineseherbcategoryService.queryById(categoryId));
    }

    /**
     * 新增中药分类
     */
    @SaCheckPermission("app:chineseherbcategory:add")
    @Log(title = "中药分类", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody Chineseherbcategory bean) {
        return toAjax(iChineseherbcategoryService.insert(bean));
    }

    /**
     * 修改中药分类
     */
    @SaCheckPermission("app:chineseherbcategory:edit")
    @Log(title = "中药分类", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody Chineseherbcategory bean) {
        return toAjax(iChineseherbcategoryService.update(bean));
    }

    /**
     * 删除中药分类
     *
     * @param categoryIds 主键串
     */
    @SaCheckPermission("app:chineseherbcategory:remove")
    @Log(title = "中药分类", businessType = BusinessType.DELETE)
    @DeleteMapping("/{categoryIds}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] categoryIds) {
        return toAjax(iChineseherbcategoryService.deleteWithValidByIds(Arrays.asList(categoryIds), true));
    }
}
