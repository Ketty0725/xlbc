package com.xlbc.web.controller.app;

import java.util.List;
import java.util.Arrays;

import com.xlbc.app.domain.Product;
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
import com.xlbc.app.service.IProductService;
import com.xlbc.common.core.page.TableDataInfo;

/**
 * 药品信息
 *
 * @author ketty
 * @date 2023-03-26
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/app/product")
public class ProductController extends BaseController {

    private final IProductService iProductService;

    /**
     * 查询药品信息列表
     */
    @SaCheckPermission("app:product:list")
    @GetMapping("/list")
    public TableDataInfo<Product> list(Product bo, PageQuery pageQuery) {
        return iProductService.queryPageList(bo, pageQuery);
    }

    @SaCheckPermission("app:product:list")
    @GetMapping("/listAll")
    public R<List<Product>> listAll() {
        return R.ok(iProductService.queryListAll());
    }

    /**
     * 导出药品信息列表
     */
    @SaCheckPermission("app:product:export")
    @Log(title = "药品信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(Product bo, HttpServletResponse response) {
        List<Product> list = iProductService.queryList(bo);
        ExcelUtil.exportExcel(list, "药品信息", Product.class, response);
    }

    /**
     * 获取药品信息详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("app:product:query")
    @GetMapping("/{id}")
    public R<Product> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(iProductService.queryById(id));
    }

    /**
     * 新增药品信息
     */
    @SaCheckPermission("app:product:add")
    @Log(title = "药品信息", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody Product bo) {
        return toAjax(iProductService.insert(bo));
    }

    /**
     * 修改药品信息
     */
    @SaCheckPermission("app:product:edit")
    @Log(title = "药品信息", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody Product bo) {
        return toAjax(iProductService.update(bo));
    }

    /**
     * 删除药品信息
     *
     * @param ids 主键串
     */
    @SaCheckPermission("app:product:remove")
    @Log(title = "药品信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(iProductService.deleteWithValidByIds(Arrays.asList(ids), true));
    }
}
