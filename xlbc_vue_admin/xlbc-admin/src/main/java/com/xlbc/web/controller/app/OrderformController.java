package com.xlbc.web.controller.app;

import java.util.List;
import java.util.Arrays;

import com.xlbc.app.domain.Orderform;
import com.xlbc.app.domain.Product;
import com.xlbc.app.domain.User;
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
import com.xlbc.app.service.IOrderformService;
import com.xlbc.common.core.page.TableDataInfo;

/**
 * 订单管理
 *
 * @author ketty
 * @date 2023-04-05
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/app/orderform")
public class OrderformController extends BaseController {

    private final IOrderformService iOrderformService;

    /**
     * 查询订单管理列表
     */
    @SaCheckPermission("app:orderform:list")
    @GetMapping("/list")
    public TableDataInfo<Orderform> list(Orderform bo, String uName, String pName, PageQuery pageQuery) {
        return iOrderformService.queryPageList(bo, uName, pName, pageQuery);
    }

    /**
     * 导出订单管理列表
     */
    @SaCheckPermission("app:orderform:export")
    @Log(title = "订单管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(Orderform bo, HttpServletResponse response) {
        List<Orderform> list = iOrderformService.queryList(bo);
        ExcelUtil.exportExcel(list, "订单管理", Orderform.class, response);
    }

    /**
     * 获取订单管理详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("app:orderform:query")
    @GetMapping("/{id}")
    public R<Orderform> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(iOrderformService.queryById(id));
    }

    /**
     * 新增订单管理
     */
    @SaCheckPermission("app:orderform:add")
    @Log(title = "订单管理", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody Orderform bo) {
        return toAjax(iOrderformService.insert(bo));
    }

    /**
     * 订单发货
     */
    @SaCheckPermission("app:orderform:edit")
    @Log(title = "订单管理", businessType = BusinessType.UPDATE)
    @PutMapping("/{id}")
    public R<Void> edit(@NotNull(message = "主键不能为空")
                            @PathVariable Long id) {
        return toAjax(iOrderformService.update(id));
    }

    /**
     * 删除订单管理
     *
     * @param ids 主键串
     */
    @SaCheckPermission("app:orderform:remove")
    @Log(title = "订单管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(iOrderformService.deleteWithValidByIds(Arrays.asList(ids), true));
    }
}
