package com.zxb.cache.dao;

import com.zxb.cache.domain.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author zhaoxb
 * @date 2020/08/06 7:49 下午
 */
@Mapper
public interface UserDao {
    User findById(Long id);
    void updateUserById(User user);
    void deleteById(Long id);
}
