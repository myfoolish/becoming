# 分布式事务解决方案
## 遵循CP    强一致性    
### XA协议
#### 什么是XA协议
XA是数据库级别的协议/规范。在TM和RM之间建立双向通讯桥梁，是TM与RM之间的通讯接口【若想要使用XA需要数据库支持XA】
#### 怎么查看是不是支持XA
```mysql
show engines;
```
### XA基本语法规范
* xa_start    开始XA
* xa_end      结束XA
* xa_prepare  进入prepare阶段
* xa_commit   提交
* xa_rollback 回滚
### 如何使用数据库支持的XA解决分布式事务
```mysql
-- 开启XA   XA START 事务id
XA START 'xwor';
-- 执行业务操作
UPDATE user1 SET money = money + 8500 WHERE id = 1;
UPDATE user2 SET money = money + 100000 WHERE id = 1;
-- 结束XA   XA END 事务id
XA END 'xwor';
-- 进入PREPARE阶段 XA PREPARE 事务id
XA PREPARE 'xwor';
SELECT * FROM user1;
-- 提交 XA COMMIT 事务id
XA COMMIT 'xwor';
-- 回滚 XA ROLLBACK 事务id
XA ROLLBACK 'xwor';
```
```java
package com.xw.proto;

import com.mysql.cj.jdbc.MysqlXADataSource;
import com.mysql.cj.jdbc.MysqlXid;

import javax.sql.XAConnection;
import javax.transaction.xa.XAException;
import javax.transaction.xa.XAResource;
import javax.transaction.xa.Xid;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author liuxiaowei
 * @description
 * @date 2023/12/20
 */
public class XA {

    private final String url1 = "jdbc:mysql://localhost:3306/becoming?useSSL=false";
    private final String url2 = "jdbc:mysql://localhost:3306/demo?useSSL=false";
    private final String username = "root";
    private final String password = "root";
    String sql1 = "UPDATE user1 SET money = money + 8500 WHERE id = 1";
    String sql2 = "UPDATE user2 SET money = money + 100000 WHERE id = 1";

    public static void main(String[] args) throws SQLException, XAException {
        XA xa = new XA();
//        xa.proto();
        xa.xa();
    }

    private void proto() throws SQLException {
        Connection connection1 = DriverManager.getConnection(url1, username, password);
        connection1.prepareStatement(sql1).execute();
//        int i = 1 / 0;
        Connection connection2 = DriverManager.getConnection(url2, username, password);
        connection2.prepareStatement(sql2).execute();
    }

    /**
     * 强一致性
     * @throws SQLException
     * @throws XAException
     */
    public void xa() throws SQLException, XAException {
        // 获取数据源
        MysqlXADataSource xw1 = new MysqlXADataSource();
        xw1.setUrl(url1);
        xw1.setUser(username);
        xw1.setPassword(password);

        MysqlXADataSource xw2 = new MysqlXADataSource();
        xw2.setUrl(url2);
        xw2.setUser(username);
        xw2.setPassword(password);
        // 获取资源管理器
        XAConnection xaConnection1 = xw1.getXAConnection();
        XAResource xaResource1 = xaConnection1.getXAResource();

        XAConnection xaConnection2 = xw2.getXAConnection();
        XAResource xaResource2 = xaConnection2.getXAResource();

        Xid xid1 = new MysqlXid("1".getBytes(), "2".getBytes(), 12);
        Xid xid2 = new MysqlXid("2".getBytes(), "1".getBytes(), 21);

        try {
            xaResource1.start(xid1, XAResource.TMNOFLAGS);
            xaResource2.start(xid2, XAResource.TMNOFLAGS);

            xaConnection1.getConnection().prepareStatement(sql1).execute();
            xaResource1.end(xid1,XAResource.TMSUCCESS);
//            int i = 1 / 0;
            xaConnection2.getConnection().prepareStatement(sql2).execute();
            xaResource2.end(xid2,XAResource.TMSUCCESS);

            int prepare1 = xaResource1.prepare(xid1);
            int prepare2 = xaResource2.prepare(xid2);

            if (prepare1 == XAResource.XA_OK && prepare2 == XAResource.XA_OK) {
                xaResource1.commit(xid1, false);
                xaResource2.commit(xid2, false);
            } else {
                xaResource1.rollback(xid1);
                xaResource2.rollback(xid2);
            }
        } catch (Exception e) {
            xaResource1.rollback(xid1);
            xaResource2.rollback(xid2);
            throw new RuntimeException(e);
        }
    }
}
```
### Seata失效场景（使用seata所遇到的问题）
1. 注册分支事务时没有传递全局事务id
    1. 手动传全局事务id
    2. 使用spring-cloud-starter-alibaba-seata
2. 版本兼容问题。在某些版本中，自动代理使用Druid数据源可用，使用默认数据源不行 https://github.com/seata/seata/issues/4844
3. 全局异常处理会导致分布式事务失效（原因：全局异常处理之后项目中将不会出现显式异常，此时TC识别不到异常，则会提交其他分支事务）
    1. 手动回滚
    2. 后台服务不需要异常处理
4. 微服务架构中降级熔断会导致分布式事务失效，其解决方案为：手动回滚，样例代码[com.xw.feign.FeignFallbackService.AccountFeignFallbackService.reduceMoney]
5. @GlobalTransactional注解写在controller中，在某些场景下会失效
6. 在application.yml中使用type属性指定DruidDataSource时，事务失效（原因：使用type属性指定的数据源，seata默认不会自动代理）


## 遵循AP    弱一致性
    TCC、AT、Saga、可靠消息最终一致性
Seata对弱一致性对支持有AT（建议使用）、TCC、Saga
### TCC协议
# 分布式事务解决规范
* DTP模型 是X/Open组织提供的一套分布式事务的标准，该标准定义了解决分布式事务的规范和接口，自身没有实现，由各个厂商实现（类似于数据库驱动）
  * AP(Application)         应用程序
  * TM(Transaction Manager) 事务管理器
  * RM(Resource Manager)    资源管理器
* 两阶段提交模型（是基于DTP模型的） 表示在规范情况下事务的完成分为两大阶段：1、prepare阶段；2、Commit/Rollback阶段
* 三阶段提交模型（很少使用）


| seata-all | 最基础的包，没有自动配置，需要使用.conf编写配置，适合ssm项目           |
|-----------|----------------------------------------------|
|seata-spring-boot-starter           | 在seata-all基础上添加了自动配置，可以使用application.yml编写配置 |
|spring-cloud-starter-alibaba-seata           | 在seata-spring-boot-starter基础上添加了全局事务id的传递    |

