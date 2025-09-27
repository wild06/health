package com.unity.back.mapper;

import com.unity.back.entity.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {
    int insertUser(User user);
    User findByUsername(@Param("username") String username);
    User findById(@Param("id") Long id);
    int updateUser(User user);
}
