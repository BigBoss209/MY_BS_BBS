package com.example.boot1907.component;

import com.example.boot1907.assist.LoginLimitCredentialsMatcher;
import com.example.boot1907.assist.SessionDaoImpl;
import com.example.boot1907.realm.UserRealm;
import com.example.boot1907.assist.LoginLimitCredentialsMatcher;
import com.example.boot1907.realm.UserRealm;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.mgt.ExecutorServiceSessionValidationScheduler;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.session.mgt.SessionValidationScheduler;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.Cookie;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.beans.factory.config.MethodInvokingFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.MethodInvoker;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {
    /**
     * 创建本地缓存器
     * @return ehCacheManager
     */
    @Bean
    public CacheManager getCacheManage() {
        EhCacheManager ehCacheManager = new EhCacheManager();
        ehCacheManager.setCacheManagerConfigFile("classpath:ehcache.xml");
        return ehCacheManager;
    }

    /**
     * 创建自定义密码凭证匹配器
     * @return LoginLimitCredentialsMatcher
     */
    @Bean
    public CredentialsMatcher credentialsMatcher(CacheManager cacheManager) {
        LoginLimitCredentialsMatcher credentialsMatcher = new LoginLimitCredentialsMatcher();
        credentialsMatcher.setCacheManager(cacheManager);
        credentialsMatcher.setHashAlgorithmName("md5");
        credentialsMatcher.setHashIterations(2); // hash算两次，增加复杂度
        credentialsMatcher.setStoredCredentialsHexEncoded(true);// 算法加入16进制
        return credentialsMatcher;
    }

    @Bean
    public Realm realm(CredentialsMatcher credentialsMatcher) {
        UserRealm userRealm = new UserRealm();
        userRealm.setCredentialsMatcher(credentialsMatcher);
        // 启用缓存
        userRealm.setCachingEnabled(true);
        // 缓存用户名和密码,在缓存范围内,登录不再查询数据库的用户名和密码
        userRealm.setAuthenticationCachingEnabled(true);
        // 对应ehcache.xml中配置
        userRealm.setAuthenticationCacheName("authenticationCache");
        userRealm.setAuthorizationCachingEnabled(true);
        userRealm.setAuthorizationCacheName("authorizationCache");
        return userRealm;
    }

    @Bean
    public Cookie cookie() {
        SimpleCookie cookie = new SimpleCookie();
        // 取个cookie的key的名字，类似jsessionid
        cookie.setName("sid");
        // 保证cookie无法被js读取，保证不被XSS攻击
        cookie.setHttpOnly(true);
        // 用于https协议安全cookie，http需要设置为false ,默认false
        // simpleCookie.setSecure(false);
        cookie.setMaxAge(-1); // 0 设置cookie无效，-1关闭浏览器，其他有效时间S
        return cookie;
    }

    /**
     * 创建周期定时器,每30分钟执行一次
     * @return sessionValidationScheduler
     */
    @Bean
    public SessionValidationScheduler sessionValidationScheduler() {
        ExecutorServiceSessionValidationScheduler sessionValidationScheduler = new ExecutorServiceSessionValidationScheduler();
        sessionValidationScheduler.setInterval(1800000);
        return sessionValidationScheduler;
    }

    @Bean
    public SessionDAO sessionDAO(RedisTemplate<Serializable, Object> redisTemplate) {
        SessionDaoImpl sessionDao = new SessionDaoImpl();
        sessionDao.setRedisTemplate(redisTemplate);
        return sessionDao;
    }

    /**
     * 创建session管理器
     * @param cookie 用于创建session的cookie
     * @param sessionValidationScheduler 周期删除不活动session.因为用户关闭浏览器,服务器的session还在.
     * @return sessionManager
     */
    @Bean
    public SessionManager sessionManager(Cookie cookie, SessionValidationScheduler sessionValidationScheduler) {
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        sessionManager.setSessionIdCookie(cookie);
        sessionManager.setGlobalSessionTimeout(1800000);
        // sessionManager.setSessionDAO(sessionDAO);
        // url中添加jsessionid/sid
        sessionManager.setSessionIdUrlRewritingEnabled(true);
        sessionManager.setSessionValidationScheduler(sessionValidationScheduler);
        return sessionManager;
    }

    /**
     * 创建shiro根对象
     * @param realm
     * @param sessionManager
     * @param cacheManager
     * @return
     */
    @Bean
    public DefaultWebSecurityManager securityManager(Realm realm, SessionManager sessionManager, CacheManager cacheManager) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(realm);
        securityManager.setCacheManager(cacheManager);
        securityManager.setSessionManager(sessionManager);
        return securityManager;
    }

    /**
     * 简化使用shiro，使用shiro提供util类
     * SecurityUtils.getSubject()
     * @param securityManager
     * @return
     */
    @Bean
    public MethodInvoker methodInvoker(SecurityManager securityManager) {
        MethodInvokingFactoryBean methodInvokingFactoryBean = new MethodInvokingFactoryBean();
        methodInvokingFactoryBean.setStaticMethod("org.apache.shiro.SecurityUtils.setSecurityManager");
        methodInvokingFactoryBean.setArguments(securityManager);
        return methodInvokingFactoryBean;
    }

    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        //没有认证跳转到的登录页面
        shiroFilterFactoryBean.setLoginUrl("/login.html");
        shiroFilterFactoryBean.setSuccessUrl("/index.html");
        //权限不足跳转的页面
        shiroFilterFactoryBean.setUnauthorizedUrl("/unauthorized.html");
        Map<String,String> map = new HashMap();
        //登出
        map.put("/login*","anon");
        map.put("/unauthorized*","anon");
        map.put("/register*","anon");
        map.put("/error*","anon");
        map.put("/logout","logout");
        map.put("/**","authc");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(map);
//        Map<String, Filter> filters = new HashMap<>();
//        FormAuthenticationFilter formAuthenticationFilter = new FormAuthenticationFilter();
//        formAuthenticationFilter.setLoginUrl("/login.html");
//        filters.put("authc", formAuthenticationFilter);
//        shiroFilterFactoryBean.setFilters(filters);
        //对所有用户认证
        return shiroFilterFactoryBean;
    }




}
