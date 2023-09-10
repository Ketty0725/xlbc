package com.xlbc.web.controller.app;

import java.util.Arrays;
import java.util.List;

import com.xlbc.app.domain.Productbasicinfo;
import com.xlbc.common.core.page.TableDataInfo;
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
import com.xlbc.app.service.IProductbasicinfoService;

/**
 * 药品说明
 *
 * @author ketty
 * @date 2023-03-31
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/app/productbasicinfo")
public class ProductbasicinfoController extends BaseController {

    private final IProductbasicinfoService iProductbasicinfoService;

    /**
     * 查询药品说明列表
     */
    @SaCheckPermission("app:productbasicinfo:list")
    @GetMapping("/list")
    public TableDataInfo<Productbasicinfo> list(Productbasicinfo bo, PageQuery pageQuery) {
        return iProductbasicinfoService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出药品说明列表
     */
    @SaCheckPermission("app:productbasicinfo:export")
    @Log(title = "药品说明", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(Productbasicinfo bo, HttpServletResponse response) {
        List<Productbasicinfo> list = iProductbasicinfoService.queryList(bo);
        ExcelUtil.exportExcel(list, "药品说明", Productbasicinfo.class, response);
    }

    /**
     * 获取药品说明详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("app:productbasicinfo:query")
    @GetMapping("/{id}")
    public R<Productbasicinfo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(iProductbasicinfoService.queryById(id));
    }

    /**
     * 新增药品说明
     */
    @SaCheckPermission("app:productbasicinfo:add")
    @Log(title = "药品说明", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody Productbasicinfo bo) {
        return toAjax(iProductbasicinfoService.insert(bo));
    }

    /**
     * 修改药品说明
     */
    @SaCheckPermission("app:productbasicinfo:edit")
    @Log(title = "药品说明", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody Productbasicinfo bo) {
        return toAjax(iProductbasicinfoService.update(bo));
    }

    /**
     * 删除药品说明
     *
     * @param ids 主键串
     */
    @SaCheckPermission("app:productbasicinfo:remove")
    @Log(title = "药品说明", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(iProductbasicinfoService.deleteWithValidByIds(Arrays.asList(ids), true));
    }
}
