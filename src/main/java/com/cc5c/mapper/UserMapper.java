package com.cc5c.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cc5c.entity.pojo.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper extends BaseMapper<User> {
}
