package com.xlbc.web.controller.app;

import java.util.List;
import java.util.Arrays;

import com.xlbc.app.domain.Shoppingaddress;
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
import com.xlbc.app.service.IShoppingaddressService;
import com.xlbc.common.core.page.TableDataInfo;

/**
 * 收货地址
 *
 * @author ketty
 * @date 2023-04-08
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/app/shoppingaddress")
public class ShoppingaddressController extends BaseController {

    private final IShoppingaddressService iShoppingaddressService;

    /**
     * 查询收货地址列表
     */
    @SaCheckPermission("app:shoppingaddress:list")
    @GetMapping("/list")
    public TableDataInfo<Shoppingaddress> list(Shoppingaddress bo, PageQuery pageQuery) {
        return iShoppingaddressService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出收货地址列表
     */
    @SaCheckPermission("app:shoppingaddress:export")
    @Log(title = "收货地址", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(Shoppingaddress bo, HttpServletResponse response) {
        List<Shoppingaddress> list = iShoppingaddressService.queryList(bo);
        ExcelUtil.exportExcel(list, "收货地址", Shoppingaddress.class, response);
    }

    /**
     * 获取收货地址详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("app:shoppingaddress:query")
    @GetMapping("/{id}")
    public R<Shoppingaddress> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(iShoppingaddressService.queryById(id));
    }

    /**
     * 新增收货地址
     */
    @SaCheckPermission("app:shoppingaddress:add")
    @Log(title = "收货地址", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody Shoppingaddress bo) {
        return toAjax(iShoppingaddressService.insert(bo));
    }

    /**
     * 修改收货地址
     */
    @SaCheckPermission("app:shoppingaddress:edit")
    @Log(title = "收货地址", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody Shoppingaddress bo) {
        return toAjax(iShoppingaddressService.update(bo));
    }

    /**
     * 删除收货地址
     *
     * @param ids 主键串
     */
    @SaCheckPermission("app:shoppingaddress:remove")
    @Log(title = "收货地址", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(iShoppingaddressService.deleteWithValidByIds(Arrays.asList(ids), true));
    }
}
