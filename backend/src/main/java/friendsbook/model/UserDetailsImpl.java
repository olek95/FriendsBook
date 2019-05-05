package friendsbook.model;

import java.util.ArrayList;
import java.util.Objects;
import org.springframework.security.core.userdetails.User;

public class UserDetailsImpl extends User {
    
    private final long id;
    
    public UserDetailsImpl(friendsbook.domain.user.User user) {
        super(user.getLogin(), user.getPassword(), new ArrayList());
        id = user.getId();
    }
    
    public Long getId() { return id; }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + Objects.hashCode(getUsername());
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof UserDetailsImpl)) {
            return false;
        }
        UserDetailsImpl user = (UserDetailsImpl) obj;
        return Objects.equals(getUsername().toLowerCase(), user.getUsername().toLowerCase());
    }
}
