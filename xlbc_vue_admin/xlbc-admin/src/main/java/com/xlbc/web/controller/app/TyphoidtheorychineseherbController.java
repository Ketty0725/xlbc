package com.xlbc.web.controller.app;

import java.util.List;
import java.util.Arrays;

import com.xlbc.app.domain.Typhoidtheorychineseherb;
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
import com.xlbc.app.service.ITyphoidtheorychineseherbService;
import com.xlbc.common.core.page.TableDataInfo;

/**
 * 中药
 *
 * @author ketty
 * @date 2023-04-02
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/app/typhoidtheorychineseherb")
public class TyphoidtheorychineseherbController extends BaseController {

    private final ITyphoidtheorychineseherbService iTyphoidtheorychineseherbService;

    /**
     * 查询中药列表
     */
    @SaCheckPermission("app:typhoidtheorychineseherb:list")
    @GetMapping("/list")
    public TableDataInfo<Typhoidtheorychineseherb> list(Typhoidtheorychineseherb bo, PageQuery pageQuery) {
        return iTyphoidtheorychineseherbService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出中药列表
     */
    @SaCheckPermission("app:typhoidtheorychineseherb:export")
    @Log(title = "中药", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(Typhoidtheorychineseherb bo, HttpServletResponse response) {
        List<Typhoidtheorychineseherb> list = iTyphoidtheorychineseherbService.queryList(bo);
        ExcelUtil.exportExcel(list, "中药", Typhoidtheorychineseherb.class, response);
    }

    /**
     * 获取中药详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("app:typhoidtheorychineseherb:query")
    @GetMapping("/{id}")
    public R<Typhoidtheorychineseherb> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(iTyphoidtheorychineseherbService.queryById(id));
    }

    /**
     * 新增中药
     */
    @SaCheckPermission("app:typhoidtheorychineseherb:add")
    @Log(title = "中药", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody Typhoidtheorychineseherb bo) {
        return toAjax(iTyphoidtheorychineseherbService.insert(bo));
    }

    /**
     * 修改中药
     */
    @SaCheckPermission("app:typhoidtheorychineseherb:edit")
    @Log(title = "中药", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody Typhoidtheorychineseherb bo) {
        return toAjax(iTyphoidtheorychineseherbService.update(bo));
    }

    /**
     * 删除中药
     *
     * @param ids 主键串
     */
    @SaCheckPermission("app:typhoidtheorychineseherb:remove")
    @Log(title = "中药", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(iTyphoidtheorychineseherbService.deleteWithValidByIds(Arrays.asList(ids), true));
    }
}
