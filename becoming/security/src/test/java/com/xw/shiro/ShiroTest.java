package com.xw.shiro;

import com.alibaba.druid.pool.DruidDataSource;
import com.xw.shiro.custom.CustomRealm;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.SimpleAccountRealm;
import org.apache.shiro.realm.jdbc.JdbcRealm;
import org.apache.shiro.realm.text.IniRealm;
import org.apache.shiro.subject.Subject;
import org.junit.Test;

/**
 * @author liuxiaowei
 * @description shiro认证
 * @date 2020/6/10 9:59
 */
public class ShiroTest {

    /**
     * shiro认证 / 授权
     * 创建SecurityManager对象
     * 主体提交认证 / 授权请求 ——> SecurityManager认证 / 授权
     * SecurityManager认证 / 授权 ——> Authenticator认证 / Authorizer授权
     * Authenticator认证 / Authorizer授权 ——> Realm认证 / Realm获取角色权限数据
     * Realm认证 / Realm获取角色权限数据——>从数据库中或者缓存中获取授权的数据
     */
    /*SimpleAccountRealm simpleAccountRealm = new SimpleAccountRealm();   //不支持添加权限
    @Before
    public void addUser() {
        simpleAccountRealm.addAccount("passerby", "123456","admin","user");
    }*/
    @Test
    public void AuthenticationAndAuthorizationTest() {

        SimpleAccountRealm simpleAccountRealm = new SimpleAccountRealm();   //不支持添加权限
        simpleAccountRealm.addAccount("passerby", "123456","admin","user");

        // 1、构建 SecurityManager 环境
        DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
        // 把 Realm 设置到当前 SecurityManager 环境中
        defaultSecurityManager.setRealm(simpleAccountRealm);
        // 2、主体提交认证请求
        SecurityUtils.setSecurityManager(defaultSecurityManager);
        Subject subject = SecurityUtils.getSubject();

        UsernamePasswordToken token = new UsernamePasswordToken("passerby", "123456");
        subject.login(token);
        System.out.println("isAuthenticated : " + subject.isAuthenticated());

//        subject.checkRole("admin");
        subject.checkRoles("admin","user");

        subject.logout();
        System.out.println("isAuthenticated : " + subject.isAuthenticated());
    }

    @Test
    public void IniRealmTest() {
        IniRealm iniRealm = new IniRealm("classpath:user.ini");
        DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
        defaultSecurityManager.setRealm(iniRealm);
        SecurityUtils.setSecurityManager(defaultSecurityManager);
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken("passerby", "123456");
        subject.login(token);
        System.out.println("isAuthenticated : " + subject.isAuthenticated());

        subject.checkRoles("root","admin");

        subject.checkPermissions("user:delete","user:update");
    }

    /*DruidDataSource dataSource = new DruidDataSource();
    {
        dataSource.setUrl("jdbc:mysql://localhost:3306/shiro?useSSL=false");
        dataSource.setUsername("root");
        dataSource.setPassword("root");
    }*/
    @Test
    public void JdbcRealmTest() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl("jdbc:mysql://localhost:3306/demo?useSSL=false");
        dataSource.setUsername("root");
        dataSource.setPassword("root");

        JdbcRealm jdbcRealm = new JdbcRealm();
        jdbcRealm.setDataSource(dataSource);
        //权限的开关 默认为 false，此时不会从数据库中查询权限数据，只有在 true 时才会去数据库查询
        jdbcRealm.setPermissionsLookupEnabled(true);

//        String sql = "select password from users where username=?";
//        jdbcRealm.setAuthenticationQuery(sql);

        //自定义查询sql
        String authenticationQuery = "select password from user where username=?";
        jdbcRealm.setAuthenticationQuery(authenticationQuery);
        String userRolesQuery = "select role_name from user_role where username=?";
        jdbcRealm.setUserRolesQuery(userRolesQuery);
        String permissionsQuery = "select permission from roles_permissions where role_name = ?";
        jdbcRealm.setPermissionsQuery(permissionsQuery);

        DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
        defaultSecurityManager.setRealm(jdbcRealm);
        SecurityUtils.setSecurityManager(defaultSecurityManager);
        Subject subject = SecurityUtils.getSubject();
//        UsernamePasswordToken token = new UsernamePasswordToken("passerby", "123456");
        UsernamePasswordToken token = new UsernamePasswordToken("xwCoding", "123");
        subject.login(token);
        System.out.println("isAuthenticated : " + subject.isAuthenticated());

//        subject.checkRoles("admin","user");
        subject.checkRoles("user");
//        subject.checkPermissions("user:add","user:delete","user:update","user:select");
        subject.checkPermissions("user:select");
    }

    // 自定义Realm 并加密
    @Test
    public void CustomRealmTest() {
        CustomRealm customRealm = new CustomRealm();
        DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
        defaultSecurityManager.setRealm(customRealm);

        // encrypt 加密
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        hashedCredentialsMatcher.setHashAlgorithmName("md5");   // 设置加密算法的名称
        hashedCredentialsMatcher.setHashIterations(1);  // 设置加密算法的次数
        customRealm.setCredentialsMatcher(hashedCredentialsMatcher);

        SecurityUtils.setSecurityManager(defaultSecurityManager);
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken("passerby", "123456");
        subject.login(token);
        System.out.println("isAuthenticated:" + subject.isAuthenticated());

        subject.checkRoles("user","admin");
        subject.checkPermissions("user:add", "user:delete");
    }
}
