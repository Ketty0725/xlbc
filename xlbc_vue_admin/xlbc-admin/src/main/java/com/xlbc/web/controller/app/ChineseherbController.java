package com.xlbc.web.controller.app;

import java.util.List;
import java.util.Arrays;

import com.xlbc.app.domain.Chineseherb;
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
import com.xlbc.app.service.IChineseherbService;
import com.xlbc.common.core.page.TableDataInfo;

/**
 * 中药数据
 *
 * @author ketty
 * @date 2023-03-20
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/app/chineseherb")
public class ChineseherbController extends BaseController {

    private final IChineseherbService iChineseherbService;

    /**
     * 查询中药数据列表
     */
    @SaCheckPermission("app:chineseherb:list")
    @GetMapping("/list")
    public TableDataInfo<Chineseherb> list(Chineseherb bean, PageQuery pageQuery) {
        return iChineseherbService.queryPageList(bean, pageQuery);
    }

    /**
     * 导出中药数据列表
     */
    @SaCheckPermission("app:chineseherb:export")
    @Log(title = "中药数据", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(Chineseherb bean, HttpServletResponse response) {
        List<Chineseherb> list = iChineseherbService.queryList(bean);
        ExcelUtil.exportExcel(list, "中药数据", Chineseherb.class, response);
    }

    /**
     * 获取中药数据详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("app:chineseherb:query")
    @GetMapping("/{id}")
    public R<Chineseherb> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(iChineseherbService.queryById(id));
    }

    /**
     * 新增中药数据
     */
    @SaCheckPermission("app:chineseherb:add")
    @Log(title = "中药数据", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody Chineseherb bean) {
        return toAjax(iChineseherbService.insert(bean));
    }

    /**
     * 修改中药数据
     */
    @SaCheckPermission("app:chineseherb:edit")
    @Log(title = "中药数据", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody Chineseherb bean) {
        return toAjax(iChineseherbService.update(bean));
    }

    /**
     * 删除中药数据
     *
     * @param ids 主键串
     */
    @SaCheckPermission("app:chineseherb:remove")
    @Log(title = "中药数据", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(iChineseherbService.deleteWithValidByIds(Arrays.asList(ids), true));
    }
}
