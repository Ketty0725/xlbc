package com.xlbc.web.controller.app;

import java.util.List;
import java.util.Arrays;

import com.xlbc.app.domain.Homebanner;
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
import com.xlbc.app.service.IHomebannerService;
import com.xlbc.common.core.page.TableDataInfo;

/**
 * 轮播图
 *
 * @author ketty
 * @date 2023-04-04
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/app/homebanner")
public class HomebannerController extends BaseController {

    private final IHomebannerService iHomebannerService;

    /**
     * 查询轮播图列表
     */
    @SaCheckPermission("app:homebanner:list")
    @GetMapping("/list")
    public TableDataInfo<Homebanner> list(Homebanner bo, PageQuery pageQuery) {
        return iHomebannerService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出轮播图列表
     */
    @SaCheckPermission("app:homebanner:export")
    @Log(title = "轮播图", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(Homebanner bo, HttpServletResponse response) {
        List<Homebanner> list = iHomebannerService.queryList(bo);
        ExcelUtil.exportExcel(list, "轮播图", Homebanner.class, response);
    }

    /**
     * 获取轮播图详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("app:homebanner:query")
    @GetMapping("/{id}")
    public R<Homebanner> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(iHomebannerService.queryById(id));
    }

    /**
     * 新增轮播图
     */
    @SaCheckPermission("app:homebanner:add")
    @Log(title = "轮播图", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody Homebanner bo) {
        return toAjax(iHomebannerService.insert(bo));
    }

    /**
     * 修改轮播图
     */
    @SaCheckPermission("app:homebanner:edit")
    @Log(title = "轮播图", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody Homebanner bo) {
        return toAjax(iHomebannerService.update(bo));
    }

    /**
     * 删除轮播图
     *
     * @param ids 主键串
     */
    @SaCheckPermission("app:homebanner:remove")
    @Log(title = "轮播图", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(iHomebannerService.deleteWithValidByIds(Arrays.asList(ids), true));
    }
}
