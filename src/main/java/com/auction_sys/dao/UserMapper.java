package com.auction_sys.dao;

import com.auction_sys.pojo.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {
    int deleteByPrimaryKey(Long userId);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Long userId);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    int updatePasswordByEmail(@Param("email") String email, @Param("password") String password);

    int updatePasswordByUserId(@Param("userId") long userId, @Param("password") String password);

    User selectByEmail(String email);

    int checkUsername(String username);

    int checkEmail(String email);

    User selectByUsername(String username);
}