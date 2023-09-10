package com.xlbc.web.controller.app;

import java.util.List;
import java.util.Arrays;

import com.xlbc.app.domain.Productdoctorqa;
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
import com.xlbc.app.service.IProductdoctorqaService;
import com.xlbc.common.core.page.TableDataInfo;

/**
 * 医生问答
 *
 * @author ketty
 * @date 2023-04-01
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/app/productdoctorqa")
public class ProductdoctorqaController extends BaseController {

    private final IProductdoctorqaService iProductdoctorqaService;

    /**
     * 查询医生问答列表
     */
    @SaCheckPermission("app:productdoctorqa:list")
    @GetMapping("/list")
    public TableDataInfo<Productdoctorqa> list(Productdoctorqa bo, PageQuery pageQuery) {
        return iProductdoctorqaService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出医生问答列表
     */
    @SaCheckPermission("app:productdoctorqa:export")
    @Log(title = "医生问答", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(Productdoctorqa bo, HttpServletResponse response) {
        List<Productdoctorqa> list = iProductdoctorqaService.queryList(bo);
        ExcelUtil.exportExcel(list, "医生问答", Productdoctorqa.class, response);
    }

    /**
     * 获取医生问答详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("app:productdoctorqa:query")
    @GetMapping("/{id}")
    public R<Productdoctorqa> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(iProductdoctorqaService.queryById(id));
    }

    /**
     * 新增医生问答
     */
    @SaCheckPermission("app:productdoctorqa:add")
    @Log(title = "医生问答", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody Productdoctorqa bo) {
        return toAjax(iProductdoctorqaService.insert(bo));
    }

    /**
     * 修改医生问答
     */
    @SaCheckPermission("app:productdoctorqa:edit")
    @Log(title = "医生问答", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody Productdoctorqa bo) {
        return toAjax(iProductdoctorqaService.update(bo));
    }

    /**
     * 删除医生问答
     *
     * @param ids 主键串
     */
    @SaCheckPermission("app:productdoctorqa:remove")
    @Log(title = "医生问答", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(iProductdoctorqaService.deleteWithValidByIds(Arrays.asList(ids), true));
    }
}
