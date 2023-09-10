package com.ketty.serviceimpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.ketty.api_entity.User;
import com.ketty.api_mapper.UserMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ketty.common_base.MinioUtil;
import com.ketty.common_utils.IDKeyUtil;
import com.ketty.common_utils.IPAddressUtil;
import com.ketty.common_utils.MD5Util;
import com.ketty.common_utils.SnowFlakeUtil;
import com.ketty.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ketty
 * @since 2023-01-15
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    private static final String HEADICON = "http://8.130.17.185:9000/chinese-medicine/default/default_head_icon.png";
    private static final String BACKIMAGE = "http://8.130.17.185:9000/chinese-medicine/default/default_background.png";
    @Autowired
    UserMapper mapper;
    @Autowired
    MinioUtil minioUtil;
    @Autowired
    CommunityService communityService;
    @Autowired
    OrderformService orderformService;
    @Autowired
    ProductshopcartService productshopcartService;
    @Autowired
    ShoppingaddressService shoppingaddressService;
    @Autowired
    ConcernService concernService;

    @Override
    public Long loginForQQ(User user) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("u_id");
        queryWrapper.eq("u_qq_id", user.getUQqId());
        User u = mapper.selectOne(queryWrapper);
        if (u == null) {
            user.setUId(SnowFlakeUtil.getId());
            user.setUName(user.getUQqName());
            user.setBackgroundImage(BACKIMAGE);
            user.setUAbout("暂时还没有简介");
            mapper.insert(user);
        }
        return mapper.selectOne(queryWrapper).getUId();
    }

    @Override
    public Long loginForCaptcha(User user) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("u_id");
        queryWrapper.eq("u_phone", user.getUPhone());
        User u = mapper.selectOne(queryWrapper);
        if (u == null) {
            user.setUId(SnowFlakeUtil.getId());
            user.setUName("用户"+user.getUPhone().substring(7));
            user.setUHeadicon(HEADICON);
            user.setBackgroundImage(BACKIMAGE);
            user.setUAbout("暂时还没有简介");
            mapper.insert(user);
        }
        return mapper.selectOne(queryWrapper).getUId();
    }

    @Override
    public Long loginForPassword(User user) {
        String password = MD5Util.getMD5(user.getUPassword());
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("u_id","u_password");
        queryWrapper.eq("u_phone", user.getUPhone());
        User u = mapper.selectOne(queryWrapper);
        if (u != null) {
            if (u.getUPassword() == null) {
                throw new RuntimeException("该账号不支持密码登录，请更换登录方式");
            } else if (u.getUPassword().equals(password)) {
                return u.getUId();
            } else {
                return null;
            }
        }
        return null;
    }

    @Override
    public void updateUserInfo(User user, MultipartFile headIconFile, MultipartFile backgroundFile) {
        User u = mapper.selectById(user.getUId());
        if (headIconFile != null) {
            String fileName = minioUtil.upload(headIconFile, "user");
            user.setUHeadicon(fileName);
            minioUtil.remove(u.getUHeadicon());
        }
        if (backgroundFile != null) {
            String fileName = minioUtil.upload(backgroundFile, "user");
            user.setBackgroundImage(fileName);
            minioUtil.remove(u.getBackgroundImage());
        }
        mapper.update(null, new LambdaUpdateWrapper<User>()
                .set(User::getUName, user.getUName())
                .set(User::getUSex, user.getUSex())
                .set(User::getUBirthday, user.getUBirthday())
                .set(User::getUAddress, user.getUAddress())
                .set(User::getUAbout, user.getUAbout())
                .set(User::getUHeadicon, user.getUHeadicon())
                .set(User::getBackgroundImage, user.getBackgroundImage())
                        .eq(User::getUId, user.getUId()));
    }

    @Override
    public User getById(Long userId) {
        User user = mapper.selectById(userId);
        return user;
    }

    @Override
    public User getByIdIsPart(Long userId) {
        User user = mapper.selectOne(new LambdaQueryWrapper<User>()
                        .select(User::getUId,User::getUName,User::getUHeadicon,User::getIpAddress)
                .eq(User::getUId,userId));
        return user;
    }

    @Override
    public List<User> selectLikeUserName(String name) {
        List<User> list = mapper.selectList(new LambdaQueryWrapper<User>()
                .like(User::getUName,name));
        return list;
    }

    @Override
    public void updateIPAddress(Long uId, String ip) {
        String ipAddress = IPAddressUtil.getIPAddresses(ip);
        if (ipAddress == null) {
            ipAddress = "未知";
        }
        mapper.update(null, new LambdaUpdateWrapper<User>()
                .set(User::getIpAddress,ipAddress)
                .eq(User::getUId,uId));
    }

    @Override
    public String getUserIP(Long uId) {
        User user = mapper.selectOne(new LambdaQueryWrapper<User>()
                        .select(User::getIpAddress)
                .eq(User::getUId,uId));
        return user.getIpAddress();
    }

    @Override
    public User selectPhoneUsed(Long uId, String uPhone) {
        User user = mapper.selectOne(new LambdaQueryWrapper<User>()
                        .select(User::getUId,User::getUName,User::getUPhone)
                .eq(User::getUPhone,uPhone));
        if (user != null) {
            if (!uId.equals(user.getUId())) {
                return user;
            }
        }
        return null;
    }

    @Override
    public void updatePhone(Long uId, String uPhone) {
        User user = selectPhoneUsed(uId,uPhone);
        if (user != null) {
            mapper.update(null, new LambdaUpdateWrapper<User>()
                    .set(User::getUPhone,"")
                    .eq(User::getUId,user.getUId()));
        }
        mapper.update(null, new LambdaUpdateWrapper<User>()
                .set(User::getUPhone,uPhone)
                .eq(User::getUId,uId));
    }

    @Override
    public void updatePassword(Long uId, String uPassword) {
        uPassword = MD5Util.getMD5(uPassword);
        mapper.update(null, new LambdaUpdateWrapper<User>()
                .set(User::getUPassword,uPassword)
                .eq(User::getUId,uId));
    }

    @Override
    public boolean verifyOldPassword(Long uId, String uPassword) {
        uPassword = MD5Util.getMD5(uPassword);
        boolean isTrue = mapper.exists(new LambdaQueryWrapper<User>()
                .eq(User::getUId,uId)
                .eq(User::getUPassword,uPassword));
        return isTrue;
    }

    @Override
    public void removeQQAcount(Long uId) {
        mapper.update(null, new LambdaUpdateWrapper<User>()
                .set(User::getUQqId,"")
                .set(User::getUQqName,"")
                .eq(User::getUId,uId));
    }

    @Override
    public User selectQQUsed(Long uId, String uQqId) {
        User user = mapper.selectOne(new LambdaQueryWrapper<User>()
                .select(User::getUId,User::getUName,User::getUQqId)
                .eq(User::getUQqId,uQqId));
        if (user != null) {
            if (!uId.equals(user.getUId())) {
                return user;
            }
        }
        return null;
    }

    @Override
    public void updateQQ(Long uId, String uQqId, String uQqName) {
        User user = selectQQUsed(uId,uQqId);
        if (user != null) {
            removeQQAcount(user.getUId());
        }
        mapper.update(null, new LambdaUpdateWrapper<User>()
                .set(User::getUQqId,uQqId)
                .set(User::getUQqName,uQqName)
                .eq(User::getUId,uId));
    }

    @Override
    public void logoutAccount(Long uId) {
        User user = getById(uId);
        if (!HEADICON.equals(user.getUHeadicon())) {
            if (!user.getUHeadicon().contains("http://thirdqq.qlogo.cn/")) {
                minioUtil.remove(user.getUHeadicon());
            }
        }
        if (!BACKIMAGE.equals(user.getBackgroundImage())) {
            minioUtil.remove(user.getBackgroundImage());
        }
        mapper.deleteById(uId);

        communityService.deleteAllByUser(uId);
        orderformService.deleteByUser(uId);
        productshopcartService.deleteByUser(uId);
        shoppingaddressService.deleteByUser(uId);
        concernService.deleteByUser(uId);
    }
}
