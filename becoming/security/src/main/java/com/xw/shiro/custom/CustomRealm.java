package com.xw.shiro.custom;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author liuxiaowei
 * @description
 * @date 2020/6/10 14:06
 */
public class CustomRealm extends AuthorizingRealm {
    /**
     * 授权
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        String username = (String) principalCollection.getPrimaryPrincipal();
        // 从数据库中或者缓存中获取角色数据
        Set<String> roles = getRolesByUsername(username);
        // 从数据库中或者缓存中获取权限数据
        Set<String> permissions = getPermissionsByUsername(username);
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        simpleAuthorizationInfo.setRoles(roles);
        simpleAuthorizationInfo.setStringPermissions(permissions);
        return simpleAuthorizationInfo;
    }

    private Set<String> getPermissionsByUsername(String username) {
        // 实际开发中访问数据库获取，此处学习，故模拟从数据库中或者缓存中获取权限数据
        Set<String> set = new HashSet<>();
        set.add("user:add");
        set.add("user:delete");
        set.add("user:update");
        set.add("user:select");
        return set;
    }

    private Set<String> getRolesByUsername(String username) {
        // 实际开发中访问数据库获取，此处学习，故模拟从数据库中或者缓存中获取角色数据
        Set<String> set = new HashSet<>();
        set.add("admin");
        set.add("user");
        return set;
    }

    Map<String, String> userMap = new HashMap<>(16);
    {
//        userMap.put("passerby", "123456");
        //加密后
//        userMap.put("passerby", "e10adc3949ba59abbe56e057f20f883e");
        //加盐后
        userMap.put("passerby", "109d6003c395b1db664036ac9345d659");
        super.setName("customRealm");
    }
    /**
     * 认证
     * @param authenticationToken 主体传过来的认证信息
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        // 1、从主体传过来的认证信息中，获取用户名
        String username = (String) authenticationToken.getPrincipal();
        // 2、通过用户名到数据库获取凭证
        String password = getPasswordByUsername(username);
        if (password == null) {
            return null;
        }
        SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo(username, password, "customRealm");
        simpleAuthenticationInfo.setCredentialsSalt(ByteSource.Util.bytes("passerby"));
        return simpleAuthenticationInfo;
    }

    private String getPasswordByUsername(String username) {
        // 实际开发需要查询数据库，此处用于学习，模拟数据库查询凭证
        return userMap.get(username);
    }

    public static void main(String[] args) {
//        Md5Hash md5Hash = new Md5Hash("123456");
        //加盐
        Md5Hash md5Hash = new Md5Hash("123456","passerby");
        System.out.println(md5Hash);
    }
}
