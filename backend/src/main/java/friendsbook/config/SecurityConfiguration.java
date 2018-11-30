package friendsbook.config;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Autowired
    DataSource dataSource;
    
    private final static String USERS_BY_USERNAME_QUERY = "SELECT login as username, password, true FROM user WHERE login = ?";
    private final static String AUTHORITIES_BY_USERNAME_QUERY = "SELECT login as username, 'ROLE_USER' FROM user WHERE login = ?";
    
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication().passwordEncoder(NoOpPasswordEncoder.getInstance())
                .dataSource(dataSource)
                .usersByUsernameQuery(USERS_BY_USERNAME_QUERY)
                .authoritiesByUsernameQuery(AUTHORITIES_BY_USERNAME_QUERY);
    }
    
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.cors().and().authorizeRequests().antMatchers("/account/login").permitAll()
                .anyRequest().authenticated().and().httpBasic();
    }
}
