package com.xw;

import com.alibaba.druid.pool.DruidDataSource;
import io.seata.rm.datasource.DataSourceProxy;
import io.seata.rm.datasource.xa.DataSourceProxyXA;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;

/**
 * @author liuxiaowei
 * @description
 * @date 2023/12/22
 */
@SpringBootApplication(exclude = {MybatisAutoConfiguration.class})
//@EnableTransactionManagement + @Transactional 只能解决单连接/数据源的事务
public class SeataMultiApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder(SeataMultiApplication.class)
                .web(WebApplicationType.SERVLET)
                .run(args);
    }

    @MapperScan(basePackages = {"com.xw.mapper.xa.mapper1"}, sqlSessionFactoryRef = "sqlSessionFactoryXA1")
    public static class DataSourceXA1 {
        @Bean
        @ConfigurationProperties(prefix = "spring.datasource.xa1")
        public DataSource dataSourceXA1() {
            return new DruidDataSource();
        }

        @Bean
        public DataSourceProxyXA dataSourceProxyXA1(@Qualifier("dataSourceXA1") DataSource dataSource) {
            return new DataSourceProxyXA(dataSource);
        }

        /**
         * 不使用seata时，此处使用的时代理数据源dataSourceXA1
         * @param dataSource    此处使用的时代理数据源dataSourceProxyXA1
         * @return
         * @throws Exception
         */
        @Bean
        public SqlSessionFactory sqlSessionFactoryXA1(@Qualifier("dataSourceProxyXA1") DataSource dataSource) throws Exception {
            SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
            sqlSessionFactoryBean.setDataSource(dataSource);
            return sqlSessionFactoryBean.getObject();
        }
    }

    @MapperScan(basePackages = {"com.xw.mapper.xa.mapper2"}, sqlSessionFactoryRef = "sqlSessionFactoryXA2")
    public static class DataSourceXA2 {
        @Bean
        @ConfigurationProperties(prefix = "spring.datasource.xa2")
        public DataSource dataSourceXA2() {
            return new DruidDataSource();
        }

        @Bean
        public DataSourceProxyXA dataSourceProxyXA2(@Qualifier("dataSourceXA2") DataSource dataSource) {
            return new DataSourceProxyXA(dataSource);
        }

        /**
         * 不使用seata时，此处使用的时代理数据源dataSourceXA2
         * @param dataSource    此处使用的时代理数据源dataSourceProxyXA2
         * @return
         * @throws Exception
         */
        @Bean
        public SqlSessionFactory sqlSessionFactoryXA2(@Qualifier("dataSourceProxyXA2") DataSource dataSource) throws Exception {
            SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
            sqlSessionFactoryBean.setDataSource(dataSource);
            return sqlSessionFactoryBean.getObject();
        }
    }

    @MapperScan(basePackages = {"com.xw.mapper.at.mapper1"}, sqlSessionFactoryRef = "sqlSessionFactoryAT1")
    public static class DataSourceAT1 {
        @Bean
        @ConfigurationProperties(prefix = "spring.datasource.at1")
        public DataSource dataSourceAT1() {
            return new DruidDataSource();
        }

        @Bean
        public DataSourceProxy dataSourceProxyAT1(@Qualifier("dataSourceAT1") DataSource dataSource) {
            return new DataSourceProxy(dataSource);
        }

        /**
         * 开启AT自动代理时，此处使用的数据源dataSourceAT1
         * @param dataSource    关闭自动代理，进行手动代理时，此处使用代理数据源dataSourceProxyAT1
         * @return
         * @throws Exception
         */
        @Bean
        public SqlSessionFactory sqlSessionFactoryAT1(@Qualifier("dataSourceProxyAT1") DataSource dataSource) throws Exception {
            SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
            sqlSessionFactoryBean.setDataSource(dataSource);
            return sqlSessionFactoryBean.getObject();
        }
    }

    @MapperScan(basePackages = {"com.xw.mapper.at.mapper2"}, sqlSessionFactoryRef = "sqlSessionFactoryAT2")
    public static class DataSourceAT2 {
        @Bean
        @ConfigurationProperties(prefix = "spring.datasource.at2")
        public DataSource dataSourceAT2() {
            return new DruidDataSource();
        }

        @Bean
        public DataSourceProxy dataSourceProxyAT2(@Qualifier("dataSourceAT2") DataSource dataSource) {
            return new DataSourceProxy(dataSource);
        }

        /**
         * 开启AT自动代理时，此处使用的数据源dataSourceAT2
         * @param dataSource    关闭自动代理，进行手动代理时，此处使用代理数据源dataSourceProxyAT2
         * @return
         * @throws Exception
         */
        @Bean
        public SqlSessionFactory sqlSessionFactoryAT2(@Qualifier("dataSourceProxyAT2") DataSource dataSource) throws Exception {
            SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
            sqlSessionFactoryBean.setDataSource(dataSource);
            return sqlSessionFactoryBean.getObject();
        }
    }
}
