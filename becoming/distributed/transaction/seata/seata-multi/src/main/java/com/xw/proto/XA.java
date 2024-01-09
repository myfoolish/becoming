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
        xa.proto();
//        xa.xa();
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
