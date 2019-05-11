package wjy.weiai7lv.config;


import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;

@Configuration
//导入properties配置文件
@PropertySource("classpath:mybatis/database.properties")
//配置mapper接口扫描,扫描Mybatis的Mapper接口
@MapperScan(basePackages = "wjy.weiai7lv.dao", sqlSessionFactoryRef = "sqlSessionFactory")
//开启事务管理
@EnableTransactionManagement
public class MyBatisConfig {


    @Value("${dataSource.driver}")
    private String driverClass;
    @Value("${dataSource.url}")
    private String jdbcUrl;
    @Value("${dataSource.username}")
    private String user;
    @Value("${dataSource.password}")
    private String password;

    @Value("${c3p0.initialPoolSize}")
    private Integer initialPoolSize;
    @Value("${c3p0.maxPoolSize}")
    private Integer maxPoolSize;
    @Value("${c3p0.minPoolSize}")
    private Integer minPoolSize;
    @Value("${c3p0.maxStatements}")
    private Integer maxStatements;
    @Value("${c3p0.acquireIncrement}")
    private Integer acquireIncrement;
    @Value("${c3p0.maxIdleTime}")
    private Integer maxIdleTime;
    @Value("${c3p0.idleConnectionTestPeriod}")
    private Integer idleConnectionTestPeriod;


    /**
     * 配置数据源
     *
     * @return
     */
    @Bean //不指定bean的名称默认使用方法名为bean的名称(id)，并首字母小写
    public ComboPooledDataSource dataSource() throws PropertyVetoException {
        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        dataSource.setAutoCommitOnClose(false);//关闭自动提交
        dataSource.setDriverClass(driverClass);
        dataSource.setJdbcUrl(jdbcUrl);
        dataSource.setUser(user);
        dataSource.setPassword(password);
        //c3p0的配置
        dataSource.setInitialPoolSize(initialPoolSize);
        dataSource.setMaxPoolSize(maxPoolSize);
        dataSource.setMinPoolSize(minPoolSize);
        dataSource.setMaxStatements(maxStatements);
        dataSource.setAcquireIncrement(acquireIncrement);
        dataSource.setMaxIdleTime(maxIdleTime);
        dataSource.setIdleConnectionTestPeriod(idleConnectionTestPeriod);
        return dataSource;
    }

    /**
     * 配置SqlSessionFactory
     *
     * @param dataSource
     * @return
     * @throws Exception
     */
    @Bean
    public SqlSessionFactory sqlSessionFactory(@Qualifier("dataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sqlSessionFactory = new SqlSessionFactoryBean();
        sqlSessionFactory.setDataSource(dataSource);
        sqlSessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mapper/*.xml"));
        sqlSessionFactory.setConfigLocation(new PathMatchingResourcePatternResolver().getResource("classpath:mybatis/mybatis-config.xml"));
        return sqlSessionFactory.getObject();
    }

    /**
     * 配置事务管理者
     *
     * @param dataSource
     * @return
     */
    @Bean
    public DataSourceTransactionManager dataSourceTransactionManager(@Qualifier("dataSource") DataSource dataSource) {
        DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager();
        dataSourceTransactionManager.setDataSource(dataSource);
        return dataSourceTransactionManager;
    }

}
