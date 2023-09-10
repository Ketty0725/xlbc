package com.xlbc.web.controller.app;

import java.util.Arrays;
import java.util.List;

import com.xlbc.app.domain.Community;
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
import com.xlbc.app.service.ICommunityService;

/**
 * 笔记
 *
 * @author ketty
 * @date 2023-03-26
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/app/community")
public class CommunityController extends BaseController {

    private final ICommunityService iCommunityService;

    /**
     * 查询笔记列表
     */
    @SaCheckPermission("app:community:list")
    @GetMapping("/list")
    public TableDataInfo<Community> list(Community bo, String uName, PageQuery pageQuery) {
        return iCommunityService.queryPageList(bo, uName, pageQuery);
    }

    /**
     * 导出笔记列表
     */
    @SaCheckPermission("app:community:export")
    @Log(title = "笔记", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(Community bo, HttpServletResponse response) {
        List<Community> list = iCommunityService.queryList(bo);
        ExcelUtil.exportExcel(list, "笔记", Community.class, response);
    }

    /**
     * 获取笔记详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("app:community:query")
    @GetMapping("/{id}")
    public R<Community> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(iCommunityService.queryById(id));
    }

    /**
     * 新增笔记
     */
    @SaCheckPermission("app:community:add")
    @Log(title = "笔记", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody Community bo) {
        return toAjax(iCommunityService.insert(bo));
    }

    /**
     * 修改笔记
     */
    @SaCheckPermission("app:community:edit")
    @Log(title = "笔记", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody Community bo) {
        return toAjax(iCommunityService.update(bo));
    }

    /**
     * 删除笔记
     *
     * @param ids 主键串
     */
    @SaCheckPermission("app:community:remove")
    @Log(title = "笔记", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(iCommunityService.deleteWithValidByIds(Arrays.asList(ids), true));
    }
}
