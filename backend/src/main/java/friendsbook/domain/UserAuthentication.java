package friendsbook.domain;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public class UserAuthentication extends UsernamePasswordAuthenticationToken {
    
    private long id;
    
    public UserAuthentication(Object principal, Object credentials) {
        super(principal, credentials);
    }
    
    public long getId() { return id; }
    
    public void setId(long id) { this.id = id; }
    
}
