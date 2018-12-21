package friendsbook.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationServiceImpl extends AbstractUserDetailsAuthenticationProvider {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    
    @Autowired
    public AuthenticationServiceImpl(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected UserDetails retrieveUser(String string, UsernamePasswordAuthenticationToken authentication) {
        String name = authentication.getName();
        UserDetails user = name.contains("@") ? 
                userService.loadUserByEmail(name) : userService.loadUserByUsername(name);
        if (user == null || !passwordEncoder.matches(authentication.getCredentials().toString(), user.getPassword())){
            throw new BadCredentialsException("Bad login/mail or password");
        }
        return user; 
    }

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {}
    
}
