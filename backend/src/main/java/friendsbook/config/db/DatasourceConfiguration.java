package friendsbook.config.db;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import java.util.Properties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

@Configuration
@EnableJpaRepositories(basePackages={"friendsbook.dao"})
public class DatasourceConfiguration {
    
    private final Environment environment;
    
    @Autowired
    public DatasourceConfiguration(Environment environment) {
        this.environment = environment;
    }
    
    @Bean
    public DataSource dataSource() {
        BasicDataSource dataSource = new  BasicDataSource();
        dataSource.setDriverClassName(environment.getProperty("jdbc.driver-class-name"));
        dataSource.setUrl(environment.getProperty("jdbc.url"));
        dataSource.setUsername(environment.getProperty("jdbc.username"));
        dataSource.setPassword(environment.getProperty("jdbc.password"));
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
        adapter.setGenerateDdl(true);
        adapter.setDatabasePlatform("org.hibernate.dialect.MySQL5InnoDBDialect");
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
