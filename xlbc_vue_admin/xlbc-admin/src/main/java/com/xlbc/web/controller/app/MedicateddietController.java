package com.xlbc.web.controller.app;

import java.util.List;
import java.util.Arrays;

import com.xlbc.app.domain.Medicateddiet;
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
import com.xlbc.app.service.IMedicateddietService;
import com.xlbc.common.core.page.TableDataInfo;

/**
 * 药膳数据
 *
 * @author ketty
 * @date 2023-04-02
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/app/medicateddiet")
public class MedicateddietController extends BaseController {

    private final IMedicateddietService iMedicateddietService;

    /**
     * 查询药膳数据列表
     */
    @SaCheckPermission("app:medicateddiet:list")
    @GetMapping("/list")
    public TableDataInfo<Medicateddiet> list(Medicateddiet bo, PageQuery pageQuery) {
        return iMedicateddietService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出药膳数据列表
     */
    @SaCheckPermission("app:medicateddiet:export")
    @Log(title = "药膳数据", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(Medicateddiet bo, HttpServletResponse response) {
        List<Medicateddiet> list = iMedicateddietService.queryList(bo);
        ExcelUtil.exportExcel(list, "药膳数据", Medicateddiet.class, response);
    }

    /**
     * 获取药膳数据详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("app:medicateddiet:query")
    @GetMapping("/{id}")
    public R<Medicateddiet> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(iMedicateddietService.queryById(id));
    }

    /**
     * 新增药膳数据
     */
    @SaCheckPermission("app:medicateddiet:add")
    @Log(title = "药膳数据", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody Medicateddiet bo) {
        return toAjax(iMedicateddietService.insert(bo));
    }

    /**
     * 修改药膳数据
     */
    @SaCheckPermission("app:medicateddiet:edit")
    @Log(title = "药膳数据", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody Medicateddiet bo) {
        return toAjax(iMedicateddietService.update(bo));
    }

    /**
     * 删除药膳数据
     *
     * @param ids 主键串
     */
    @SaCheckPermission("app:medicateddiet:remove")
    @Log(title = "药膳数据", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(iMedicateddietService.deleteWithValidByIds(Arrays.asList(ids), true));
    }
}
