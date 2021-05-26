package net.zhaoxiaobin.cache.cache;

import net.zhaoxiaobin.cache.domain.User;

/**
 * @author zhaoxb
 * @date 2020/08/07 10:57 上午
 */
public interface UserCacheService {
    User findById(Long id);
    User updateUserById(User user);
    void deleteById(Long id);
}
