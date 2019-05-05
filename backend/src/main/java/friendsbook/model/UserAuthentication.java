package friendsbook.model;

import java.util.Collections;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class UserAuthentication extends UsernamePasswordAuthenticationToken {
    
    private long id;
    
    public UserAuthentication(Object principal, Object credentials) {
        super(principal, credentials, Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
    }
    
    public long getId() { return id; }
    
    public void setId(long id) { this.id = id; }
    
}
