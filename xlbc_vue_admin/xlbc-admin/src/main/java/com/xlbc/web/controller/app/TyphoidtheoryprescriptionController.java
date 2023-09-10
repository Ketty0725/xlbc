package com.xlbc.web.controller.app;

import java.util.List;
import java.util.Arrays;

import com.xlbc.app.domain.Typhoidtheoryprescription;
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
import com.xlbc.app.service.ITyphoidtheoryprescriptionService;
import com.xlbc.common.core.page.TableDataInfo;

/**
 * 经方
 *
 * @author ketty
 * @date 2023-04-02
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/app/typhoidtheoryprescription")
public class TyphoidtheoryprescriptionController extends BaseController {

    private final ITyphoidtheoryprescriptionService iTyphoidtheoryprescriptionService;

    /**
     * 查询经方列表
     */
    @SaCheckPermission("app:typhoidtheoryprescription:list")
    @GetMapping("/list")
    public TableDataInfo<Typhoidtheoryprescription> list(Typhoidtheoryprescription bo, PageQuery pageQuery) {
        return iTyphoidtheoryprescriptionService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出经方列表
     */
    @SaCheckPermission("app:typhoidtheoryprescription:export")
    @Log(title = "经方", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(Typhoidtheoryprescription bo, HttpServletResponse response) {
        List<Typhoidtheoryprescription> list = iTyphoidtheoryprescriptionService.queryList(bo);
        ExcelUtil.exportExcel(list, "经方", Typhoidtheoryprescription.class, response);
    }

    /**
     * 获取经方详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("app:typhoidtheoryprescription:query")
    @GetMapping("/{id}")
    public R<Typhoidtheoryprescription> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(iTyphoidtheoryprescriptionService.queryById(id));
    }

    /**
     * 新增经方
     */
    @SaCheckPermission("app:typhoidtheoryprescription:add")
    @Log(title = "经方", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody Typhoidtheoryprescription bo) {
        return toAjax(iTyphoidtheoryprescriptionService.insert(bo));
    }

    /**
     * 修改经方
     */
    @SaCheckPermission("app:typhoidtheoryprescription:edit")
    @Log(title = "经方", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody Typhoidtheoryprescription bo) {
        return toAjax(iTyphoidtheoryprescriptionService.update(bo));
    }

    /**
     * 删除经方
     *
     * @param ids 主键串
     */
    @SaCheckPermission("app:typhoidtheoryprescription:remove")
    @Log(title = "经方", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(iTyphoidtheoryprescriptionService.deleteWithValidByIds(Arrays.asList(ids), true));
    }
}
