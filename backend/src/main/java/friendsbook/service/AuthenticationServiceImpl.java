package friendsbook.service;

import org.apache.commons.validator.EmailValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationServiceImpl extends AbstractUserDetailsAuthenticationProvider {
    private final UserService userService;
    
    Logger logger = LoggerFactory.getLogger(AuthenticationServiceImpl.class);
    
    @Autowired
    public AuthenticationServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    protected UserDetails retrieveUser(String string, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        String name = authentication.getName();
        UserDetails user = EmailValidator.getInstance().isValid(name) ? 
                userService.loadUserByEmail(name) : userService.loadUserByUsername(name);
        if (user == null || !((String)authentication.getCredentials()).equals(user.getPassword())){
            throw new BadCredentialsException("Bad credentials");
        }
        return user; 
    }

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {}
    
}
