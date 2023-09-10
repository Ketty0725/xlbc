package com.ketty.service;

import com.ketty.api_entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ketty
 * @since 2023-01-15
 */
public interface UserService extends IService<User> {
    Long loginForQQ(User user);

    Long loginForCaptcha(User user);

    Long loginForPassword(User user);

    void updateUserInfo(User user, MultipartFile headIconFile, MultipartFile backgroundFile);

    User getById(Long userId);

    User getByIdIsPart(Long userId);

    List<User> selectLikeUserName(String name);

    void updateIPAddress(Long uId, String ip);

    String getUserIP(Long uId);

    User selectPhoneUsed(Long uId, String uPhone);

    void updatePhone(Long uId, String uPhone);

    void updatePassword(Long uId, String uPassword);

    boolean verifyOldPassword(Long uId, String uPassword);

    void removeQQAcount(Long uId);

    User selectQQUsed(Long uId, String uQqId);

    void updateQQ(Long uId, String uQqId, String uQqName);

    void logoutAccount(Long uId);
}
