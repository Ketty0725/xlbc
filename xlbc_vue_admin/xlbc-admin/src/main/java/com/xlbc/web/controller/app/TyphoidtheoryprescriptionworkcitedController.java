package com.xlbc.web.controller.app;

import java.util.List;
import java.util.Arrays;

import com.xlbc.app.domain.Typhoidtheoryprescriptionworkcited;
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
import com.xlbc.app.service.ITyphoidtheoryprescriptionworkcitedService;
import com.xlbc.common.core.page.TableDataInfo;

/**
 * 相关条文
 *
 * @author ketty
 * @date 2023-04-02
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/app/typhoidtheoryprescriptionworkcited")
public class TyphoidtheoryprescriptionworkcitedController extends BaseController {

}
