package net.zhaoxiaobin.cache.cache;

import net.zhaoxiaobin.cache.dao.UserDao;
import net.zhaoxiaobin.cache.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author zhaoxb
 * @date 2020/08/07 2:15 下午
 */
@Service
@CacheConfig(cacheNames = "users") // 缓存名称，也是key前缀
public class UserCacheServiceImpl implements UserCacheService {
    @Autowired
    private UserDao userDao;

    @Override
    @Cacheable(key = "#p0") // 取第一个参数作为key的一部分，比如users::1
    public User findById(Long id) {
        return userDao.findById(id);
    }

    @Override
    @CachePut(key = "#p0.id") // 访问bean属性
    @Transactional
    public User updateUserById(User user) {
        userDao.updateUserById(user);
        return user;
    }

    @Override
    @CacheEvict(key = "#id") // 可以用形参名表示key，allEntries = true 则代表 users:: 开头的键全部删除
    @Transactional
    public void deleteById(Long id) {
        userDao.deleteById(id);
    }
}