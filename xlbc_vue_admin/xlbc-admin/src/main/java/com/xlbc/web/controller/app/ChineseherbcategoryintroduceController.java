package com.xlbc.web.controller.app;

import java.util.List;
import java.util.Arrays;

import com.xlbc.app.domain.Chineseherbcategoryintroduce;
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
import com.xlbc.app.service.IChineseherbcategoryintroduceService;
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
@RequestMapping("/app/chineseherbcategoryintroduce")
public class ChineseherbcategoryintroduceController extends BaseController {

    private final IChineseherbcategoryintroduceService iChineseherbcategoryintroduceService;

    /**
     * 查询分类介绍列表
     */
    @SaCheckPermission("app:chineseherbcategoryintroduce:list")
    @GetMapping("/list")
    public TableDataInfo<Chineseherbcategoryintroduce> list(Chineseherbcategoryintroduce bo, PageQuery pageQuery) {
        return iChineseherbcategoryintroduceService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出分类介绍列表
     */
    @SaCheckPermission("app:chineseherbcategoryintroduce:export")
    @Log(title = "分类介绍", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(Chineseherbcategoryintroduce bo, HttpServletResponse response) {
        List<Chineseherbcategoryintroduce> list = iChineseherbcategoryintroduceService.queryList(bo);
        ExcelUtil.exportExcel(list, "分类介绍", Chineseherbcategoryintroduce.class, response);
    }

    /**
     * 获取分类介绍详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("app:chineseherbcategoryintroduce:query")
    @GetMapping("/{id}")
    public R<Chineseherbcategoryintroduce> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(iChineseherbcategoryintroduceService.queryById(id));
    }

    /**
     * 新增分类介绍
     */
    @SaCheckPermission("app:chineseherbcategoryintroduce:add")
    @Log(title = "分类介绍", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody Chineseherbcategoryintroduce bo) {
        return toAjax(iChineseherbcategoryintroduceService.insert(bo));
    }

    /**
     * 修改分类介绍
     */
    @SaCheckPermission("app:chineseherbcategoryintroduce:edit")
    @Log(title = "分类介绍", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody Chineseherbcategoryintroduce bo) {
        return toAjax(iChineseherbcategoryintroduceService.update(bo));
    }

    /**
     * 删除分类介绍
     *
     * @param ids 主键串
     */
    @SaCheckPermission("app:chineseherbcategoryintroduce:remove")
    @Log(title = "分类介绍", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(iChineseherbcategoryintroduceService.deleteWithValidByIds(Arrays.asList(ids), true));
    }
}
