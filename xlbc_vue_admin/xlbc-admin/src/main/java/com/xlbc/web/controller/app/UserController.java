package com.xlbc.web.controller.app;

import java.util.List;
import java.util.Arrays;

import com.xlbc.app.domain.User;
import lombok.RequiredArgsConstructor;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.*;
import cn.dev33.satoken.annotation.SaCheckPermission;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;
import com.xlbc.common.annotation.Log;
import com.xlbc.common.core.controller.BaseController;
import com.xlbc.common.core.domain.PageQuery;
import com.xlbc.common.core.domain.R;
import com.xlbc.common.enums.BusinessType;
import com.xlbc.common.utils.poi.ExcelUtil;
import com.xlbc.app.service.IUserService;
import com.xlbc.common.core.page.TableDataInfo;

/**
 * 用户
 *
 * @author ketty
 * @date 2023-03-18
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/app/user")
public class UserController extends BaseController {

    private final IUserService iUserService;

    /**
     * 查询用户列表
     */
    @SaCheckPermission("app:user:list")
    @GetMapping("/list")
    public TableDataInfo<User> list(User bean, PageQuery pageQuery) {
        return iUserService.queryPageList(bean, pageQuery);
    }

    /**
     * 导出用户列表
     */
    @SaCheckPermission("app:user:export")
    @Log(title = "用户", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(User bean, HttpServletResponse response) {
        List<User> list = iUserService.queryList(bean);
        ExcelUtil.exportExcel(list, "用户", User.class, response);
    }

    /**
     * 获取用户详细信息
     *
     * @param uId 主键
     */
    @SaCheckPermission("app:user:query")
    @GetMapping("/{uId}")
    public R<User> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long uId) {
        return R.ok(iUserService.queryById(uId));
    }

    /**
     * 删除用户
     *
     * @param uIds 主键串
     */
    @SaCheckPermission("app:user:remove")
    @Log(title = "用户", businessType = BusinessType.DELETE)
    @DeleteMapping("/{uIds}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] uIds) {
        return toAjax(iUserService.deleteWithValidByIds(Arrays.asList(uIds), true));
    }
}
