package com.example.boot1907.realm;

import com.example.boot1907.dao.IUserDao;
import com.example.boot1907.pojo.User;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

public class UserRealm extends AuthorizingRealm {
    @Autowired
    private IUserDao userDao;

    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        System.out.println("^^^^^^^^^^^^^^执行了授权方法^^^^^^^^^^");
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        authorizationInfo.setRoles(userDao.getUserRole
                ((String)principals.getPrimaryPrincipal()));
        authorizationInfo.setStringPermissions(userDao.getUserPermission
                ((String)principals.getPrimaryPrincipal()));
        return authorizationInfo;
    }

    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        System.out.println("------执行了认证方法------");
        User dbUser = userDao.findOne(token.getPrincipal().toString());
        if(dbUser == null) {
            throw new UnknownAccountException();
        }
        if(dbUser.getValid() == 0) {
            throw new LockedAccountException();
        }
        // 把正确的用户名和密码给到匹配器,当调用subject.login的时候
        // 匹配器会去自动匹配
        return new SimpleAuthenticationInfo(dbUser.getUserName(),
                dbUser.getPasswd(),
                ByteSource.Util.bytes(dbUser.getSalt()),
                getName());
    }
}
