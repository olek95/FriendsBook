package friendsbook.config;

import javax.sql.DataSource;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
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
}
