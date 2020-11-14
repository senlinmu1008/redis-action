package com.zxb.cache.web;

import com.zxb.cache.cache.UserCacheService;
import com.zxb.cache.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhaoxb
 * @date 2020/08/07 10:50 上午
 */
@RestController
public class CacheController {
    @Autowired
    private UserCacheService userCacheService;

    @RequestMapping("/findById")
    public User findById(Long id) {
        return userCacheService.findById(id);
    }

    @RequestMapping("/updateUserById")
    public void updateUserById(User user) {
        userCacheService.updateUserById(user);
    }

    @RequestMapping("/deleteById")
    public void deleteById(Long id) {
        userCacheService.deleteById(id);
    }
}