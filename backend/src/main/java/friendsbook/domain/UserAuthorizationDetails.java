package friendsbook.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class UserAuthorizationDetails implements UserDetails {
    private User user;
    
    public UserAuthorizationDetails(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return new ArrayList();
    }

    @Override
    public String getPassword() { return user.getPassword(); }

    @Override
    public String getUsername() { return user.getLogin(); }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }

    @Override
    public int hashCode() {
        return 145 + Objects.hashCode(user);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof UserAuthorizationDetails)) {
            return false;
        }
        UserAuthorizationDetails userDetails = (UserAuthorizationDetails)obj;
        return Objects.equals(user, userDetails.user);
    }
}
