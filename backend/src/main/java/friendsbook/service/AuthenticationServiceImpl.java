package friendsbook.service;

import friendsbook.model.UserAuthentication;
import friendsbook.model.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.Authentication;
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
        Object credentials = authentication.getCredentials();
        if (user == null || (credentials != null && !passwordEncoder.matches(credentials.toString(), user.getPassword()))){
            throw new BadCredentialsException("Bad login/mail or password");
        }
        return user; 
    }
    
    @Override
    protected Authentication createSuccessAuthentication(Object principal, Authentication authentication, UserDetails user) {
        super.createSuccessAuthentication(principal, authentication, user);
        ((UserAuthentication)authentication).setId(((UserDetailsImpl)user).getId());
        return authentication;
    }

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {}
    
}
