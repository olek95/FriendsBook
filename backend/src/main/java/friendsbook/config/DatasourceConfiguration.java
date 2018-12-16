package friendsbook.config;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import java.util.Properties;

@Configuration
@EnableJpaRepositories(basePackages={"friendsbook.dao"})
public class DatasourceConfiguration {
    @Autowired
    Environment environment;
    
    private static final String ENV_DRIVER_CLASS_NAME = "jdbc.driver-class-name",
            ENV_URL = "jdbc.url", ENV_USERNAME = "jdbc.username", 
            ENV_PASSWORD = "jdbc.password";
    
    @Bean
    public DataSource dataSource() {
        BasicDataSource dataSource = new  BasicDataSource();
        dataSource.setDriverClassName(environment.getProperty(ENV_DRIVER_CLASS_NAME));
        dataSource.setUrl(environment.getProperty(ENV_URL));
        dataSource.setUsername(environment.getProperty(ENV_USERNAME));
        dataSource.setPassword(environment.getProperty(ENV_PASSWORD));
        return dataSource;
    }
    
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource, 
            JpaVendorAdapter jpaVendorAdapter) {
        Properties properties = new Properties();
        properties.put("hibernate.physical_naming_strategy", PhysicalNamingStrategyImpl.class.getName());
        LocalContainerEntityManagerFactoryBean container = new LocalContainerEntityManagerFactoryBean();
        container.setDataSource(dataSource);
        container.setJpaVendorAdapter(jpaVendorAdapter);
        container.setPackagesToScan("friendsbook.domain");
        container.setJpaProperties(properties);
        return container;
    }
    
    @Bean
    public JpaVendorAdapter jpaVendorAdapter() {
        HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
        adapter.setDatabase(Database.MYSQL);
        adapter.setShowSql(true);
        adapter.setGenerateDdl(false);
        adapter.setDatabasePlatform("org.hibernate.dialect.MySQL5Dialect");
        return adapter;
    }
    
    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager manager = new JpaTransactionManager();
        manager.setEntityManagerFactory(entityManagerFactory);
        return manager;
    }
    
    @Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation(){
        return new PersistenceExceptionTranslationPostProcessor();
    }
}
