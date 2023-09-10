package com.ketty.api_web.controller;

import com.ketty.api_entity.Concern;
import com.ketty.api_entity.User;
import com.ketty.common_utils.R;
import com.ketty.provider_sms.SMSUtil;
import com.ketty.provider_sms.SegmentUtil;
import com.ketty.service.ConcernService;
import com.ketty.service.UserService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author ketty
 * @since 2023-01-15
 */
@Api(tags = "用户管理")
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService service;
    @Autowired
    ConcernService concernService;

    @PostMapping("/loginForQQ")
    public R loginForQQ(User user) {
        Long uId = service.loginForQQ(user);
        return R.ok().data("result", uId);
    }

    @PostMapping("/sendSMS")
    public R sendSMS(String uPhone) {
        if (SegmentUtil.compare(uPhone)) {
            String captcha = String.valueOf((int)((Math.random()*9+1)*100000));
            SMSUtil.sendSMS(uPhone, captcha);
            return R.ok().data("result",captcha);
        } else {
            return R.error();
        }
    }

    @PostMapping("/loginForCaptcha")
    public R loginForCaptcha(User user) {
        Long uId = service.loginForCaptcha(user);
        return R.ok().data("result", uId);
    }

    @PostMapping("/loginForPassword")
    public R loginForPassword(User user) {
        try {
            Long uId = service.loginForPassword(user);
            if (uId == null) {
                return R.error().message("账号或密码错误");
            } else {
                return R.ok().data("result", uId);
            }
        } catch (Exception e) {
            return R.error().message(e.getMessage());
        }
    }

    @PostMapping("/updateUserInfo")
    public R updateUserInfo(User user,
                            @RequestParam(value = "headIconFile",required = false) MultipartFile headIconFile,
                            @RequestParam(value = "backgroundFile",required = false) MultipartFile backgroundFile) {
        if (user != null) {
            service.updateUserInfo(user, headIconFile, backgroundFile);
            return R.ok();
        }
        return R.error();
    }

    @PostMapping("/getUserInfo")
    public R getUserInfo(Long uId) {
        if (uId != null) {
            User user = service.getById(uId);
            return R.ok().data("result",user);
        }
        return R.error();
    }

    @PostMapping("/selectLikeUserName")
    public R selectLikeUserName(String name, Long mineId) {
        if (name != null) {
            List<User> users = service.selectLikeUserName(name);
            List<Integer> states = new ArrayList<>();
            for (User user: users) {
                Concern concern = new Concern(mineId, user.getUId());
                states.add(concernService.selectIsConcerned(concern));
            }
            return R.ok().data("users",users).data("states",states);
        }
        return R.error();
    }

    @PostMapping("/updateIPAddress")
    public R updateIPAddress(Long uId, String ip) {
        if (uId != null) {
            service.updateIPAddress(uId, ip);
            return R.ok();
        }
        return R.error();
    }

    @PostMapping("/getUserIP")
    public R getUserIP(Long uId) {
        if (uId != null) {
            String ip = service.getUserIP(uId);
            return R.ok().data("result",ip);
        }
        return R.error();
    }

    @PostMapping("/selectPhoneUsed")
    public R selectPhoneUsed(Long uId, String uPhone) {
        if (uId != null && uPhone != null) {
            User user = service.selectPhoneUsed(uId,uPhone);
            return R.ok().data("result",user);
        }
        return R.error();
    }

    @PostMapping("/updatePhone")
    public R updatePhone(Long uId, String uPhone) {
        if (uId != null && uPhone != null) {
            service.updatePhone(uId,uPhone);
            return R.ok();
        }
        return R.error();
    }

    @PostMapping("/updatePassword")
    public R updatePassword(Long uId, String uPassword) {
        if (uId != null && uPassword != null) {
            service.updatePassword(uId,uPassword);
            return R.ok();
        }
        return R.error();
    }

    @PostMapping("/verifyOldPassword")
    public R verifyOldPassword(Long uId, String uPassword) {
        if (uId != null && uPassword != null) {
            boolean isTrue = service.verifyOldPassword(uId,uPassword);
            if (isTrue) {
                return R.ok();
            }
        }
        return R.error();
    }

    @PostMapping("/removeQQAcount")
    public R removeQQAcount(Long uId) {
        if (uId != null) {
            service.removeQQAcount(uId);
            return R.ok();
        }
        return R.error();
    }

    @PostMapping("/selectQQUsed")
    public R selectQQUsed(Long uId, String uQqId) {
        if (uId != null && uQqId != null) {
            User user = service.selectQQUsed(uId,uQqId);
            return R.ok().data("result",user);
        }
        return R.error();
    }

    @PostMapping("/updateQQ")
    public R updateQQ(Long uId, String uQqId, String uQqName) {
        if (uId != null && uQqId != null && uQqName != null) {
            service.updateQQ(uId,uQqId,uQqName);
            return R.ok();
        }
        return R.error();
    }

    @PostMapping("/logoutAccount")
    public R logoutAccount(Long uId) {
        if (uId != null) {
            service.logoutAccount(uId);
            return R.ok();
        }
        return R.error();
    }

}
