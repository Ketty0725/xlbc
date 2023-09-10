package com.xlbc.web.controller.app;

import java.util.List;
import java.util.Arrays;

import com.xlbc.app.domain.Chinesepatentmedicine;
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
import com.xlbc.app.service.IChinesepatentmedicineService;
import com.xlbc.common.core.page.TableDataInfo;

/**
 * 中成药数据
 *
 * @author ketty
 * @date 2023-04-02
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/app/chinesepatentmedicine")
public class ChinesepatentmedicineController extends BaseController {

    private final IChinesepatentmedicineService iChinesepatentmedicineService;

    /**
     * 查询中成药数据列表
     */
    @SaCheckPermission("app:chinesepatentmedicine:list")
    @GetMapping("/list")
    public TableDataInfo<Chinesepatentmedicine> list(Chinesepatentmedicine bo, PageQuery pageQuery) {
        return iChinesepatentmedicineService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出中成药数据列表
     */
    @SaCheckPermission("app:chinesepatentmedicine:export")
    @Log(title = "中成药数据", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(Chinesepatentmedicine bo, HttpServletResponse response) {
        List<Chinesepatentmedicine> list = iChinesepatentmedicineService.queryList(bo);
        ExcelUtil.exportExcel(list, "中成药数据", Chinesepatentmedicine.class, response);
    }

    /**
     * 获取中成药数据详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("app:chinesepatentmedicine:query")
    @GetMapping("/{id}")
    public R<Chinesepatentmedicine> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(iChinesepatentmedicineService.queryById(id));
    }

    /**
     * 新增中成药数据
     */
    @SaCheckPermission("app:chinesepatentmedicine:add")
    @Log(title = "中成药数据", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody Chinesepatentmedicine bo) {
        return toAjax(iChinesepatentmedicineService.insert(bo));
    }

    /**
     * 修改中成药数据
     */
    @SaCheckPermission("app:chinesepatentmedicine:edit")
    @Log(title = "中成药数据", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody Chinesepatentmedicine bo) {
        return toAjax(iChinesepatentmedicineService.update(bo));
    }

    /**
     * 删除中成药数据
     *
     * @param ids 主键串
     */
    @SaCheckPermission("app:chinesepatentmedicine:remove")
    @Log(title = "中成药数据", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(iChinesepatentmedicineService.deleteWithValidByIds(Arrays.asList(ids), true));
    }
}
