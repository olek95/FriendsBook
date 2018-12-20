package friendsbook.config.security;

import friendsbook.filter.AuthenticationFilter;
import friendsbook.filter.CorsFilter;
import friendsbook.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    private AuthenticationProvider authenticationProvider;
    Logger logger = LoggerFactory.getLogger(SecurityConfiguration.class);
    
    @Autowired
    UserService userService;
    
    @Autowired
    public void setAuthenticationProvider(AuthenticationProvider authenticationProvider) {
        this.authenticationProvider = authenticationProvider;
    }
    
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider);
    }
    
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().addFilterBefore(new CorsFilter(), ChannelProcessingFilter.class)
                .authorizeRequests().antMatchers("/account/register").permitAll()
                .anyRequest().authenticated().and()
                .addFilterBefore(authenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .httpBasic();
    }
    
    @Bean
    public AuthenticationFilter authenticationFilter() throws Exception {
        AuthenticationFilter filter = new AuthenticationFilter();
        filter.setAuthenticationSuccessHandler((request, response, authentication) 
                ->  response.setStatus(HttpStatus.OK.value()));
        filter.setAuthenticationFailureHandler((request, response, authenticationException)
                -> response.setStatus(HttpStatus.UNAUTHORIZED.value()));
        filter.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/account/login", HttpMethod.GET.toString()));
        filter.setAuthenticationManager(authenticationManagerBean());
        return filter;
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}