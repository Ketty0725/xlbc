package com.ketty.api_mapper;

import com.ketty.api_entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author ketty
 * @since 2023-01-15
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
    /*int updateUserInfo(String uName,String uSex,String uHeadicon,
                       String uBirthday,String uAddress,String uAbout,Long uId);*/
}
