package com.example.boot1907.assist;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;

import java.util.concurrent.atomic.AtomicInteger;

public class LoginLimitCredentialsMatcher extends HashedCredentialsMatcher {
    private CacheManager cacheManager;
    public void setCacheManager(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        Cache<String, AtomicInteger> passwordRetryCache = cacheManager.getCache("passwordRetryCache");
        String username = (String) token.getPrincipal(); // 获取用户输入的用户名
        AtomicInteger atomicInteger = passwordRetryCache.get(username);
        if (atomicInteger == null) {
            atomicInteger = new AtomicInteger(0);
            passwordRetryCache.put(username, atomicInteger);
        }
        int count = atomicInteger.incrementAndGet(); // 增长一次，并返回增长后的值
        if (count > 5) {
            throw new ExcessiveAttemptsException();
        }
        // 拿用户的123456去通过算法,然后和数据库的密码比较
        boolean isMatch = super.doCredentialsMatch(token, info);
        if (isMatch) {
            // 如果中途密码对了，就清除错误记录
            passwordRetryCache.remove(username);
        }
        return isMatch;
    }
}
