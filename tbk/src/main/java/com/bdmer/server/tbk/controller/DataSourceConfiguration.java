package com.bdmer.server.tbk.controller;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

/**
 * 主库数据源配置（非切库组件）
 * 本类用于设置非切库访问DB时的关联配置
 * spring.datasource是指application.properties文件里的数据源连接配置。
 * classpath:/mapper/base/*Mapper.xml是指非切库DAO类对应的Mapper配置扫描路径。
 *
 * @author 龚德浪
 * @since 2018年7月16日 下午2:12:37
 */
@Configuration
@EnableTransactionManagement
@MapperScan(basePackages = {"com.bdmer.server.tbk.dao"}, sqlSessionFactoryRef = "sqlSessionFactory")
public class DataSourceConfiguration {

    /**
     * 构建数据源配置,使用@RefreshScope添加配置刷新注解，
     * 当主数据源的配置变更时，Bean将使用新配置并重新加载Bean
     *
     * @return 数据源对象
     */
    @Bean
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

    /**
     * 设置session工厂
     *
     * @param dataSource 数据源对象
     * @return session工厂对象
     * @throws Exception
     */
    @Bean(name = "sqlSessionFactory")
    public SqlSessionFactory baseSessionFactory(@Qualifier("dataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean f = new SqlSessionFactoryBean();
        f.setDataSource(dataSource);
        f.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mapper/*/*Mapper.xml"));
        SqlSessionFactory factory = f.getObject();

        // 添加自定义的一些配置
        // --> Mapper映射字段转为驼峰
        factory.getConfiguration().setMapUnderscoreToCamelCase(true);

        return factory;
    }

    /**
     * 主库事务管理器
     *
     * @param dataSource 数据源对象
     * @return 主库事务管理器对象
     */
    @Bean(name = "transactionManager")
    public DataSourceTransactionManager transactionManager(@Qualifier("dataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}
