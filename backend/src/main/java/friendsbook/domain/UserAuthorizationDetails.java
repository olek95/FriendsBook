package friendsbook.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class UserAuthorizationDetails implements UserDetails {
    private String login, password;
    
    public UserAuthorizationDetails(User user) {
        this.login = user.getLogin();
        this.password = user.getPassword();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return new ArrayList();
    }

    @Override
    public String getPassword() { return password; }

    @Override
    public String getUsername() { return login; }

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
        int hash = 7;
        hash = 53 * hash + Objects.hashCode(login);
        hash = 53 * hash + Objects.hashCode(password);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof UserAuthorizationDetails)) {
            return false;
        }
        UserAuthorizationDetails user = (UserAuthorizationDetails) obj;
        return Objects.equals(login.toLowerCase(), user.login.toLowerCase()) && Objects.equals(password, user.password);
    }
}
