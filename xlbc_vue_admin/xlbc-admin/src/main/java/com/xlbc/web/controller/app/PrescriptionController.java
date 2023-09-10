package com.xlbc.web.controller.app;

import java.util.List;
import java.util.Arrays;

import com.xlbc.app.domain.Prescription;
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
import com.xlbc.app.service.IPrescriptionService;
import com.xlbc.common.core.page.TableDataInfo;

/**
 * 方剂数据
 *
 * @author ketty
 * @date 2023-04-01
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/app/prescription")
public class PrescriptionController extends BaseController {

    private final IPrescriptionService iPrescriptionService;

    /**
     * 查询方剂数据列表
     */
    @SaCheckPermission("app:prescription:list")
    @GetMapping("/list")
    public TableDataInfo<Prescription> list(Prescription bo, PageQuery pageQuery) {
        return iPrescriptionService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出方剂数据列表
     */
    @SaCheckPermission("app:prescription:export")
    @Log(title = "方剂数据", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(Prescription bo, HttpServletResponse response) {
        List<Prescription> list = iPrescriptionService.queryList(bo);
        ExcelUtil.exportExcel(list, "方剂数据", Prescription.class, response);
    }

    /**
     * 获取方剂数据详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("app:prescription:query")
    @GetMapping("/{id}")
    public R<Prescription> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(iPrescriptionService.queryById(id));
    }

    /**
     * 新增方剂数据
     */
    @SaCheckPermission("app:prescription:add")
    @Log(title = "方剂数据", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody Prescription bo) {
        return toAjax(iPrescriptionService.insert(bo));
    }

    /**
     * 修改方剂数据
     */
    @SaCheckPermission("app:prescription:edit")
    @Log(title = "方剂数据", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody Prescription bo) {
        return toAjax(iPrescriptionService.update(bo));
    }

    /**
     * 删除方剂数据
     *
     * @param ids 主键串
     */
    @SaCheckPermission("app:prescription:remove")
    @Log(title = "方剂数据", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(iPrescriptionService.deleteWithValidByIds(Arrays.asList(ids), true));
    }
}
