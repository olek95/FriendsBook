package friendsbook.service;

import friendsbook.dao.UserRepository;
import friendsbook.domain.User;
import friendsbook.domain.UserAuthorizationDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository; 
    
    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository; 
    }
    
    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        User user = userRepository.findByLogin(login); 
        if (user == null) throw new UsernameNotFoundException("User not found");
        return new UserAuthorizationDetails(user);
    }
    
    public UserDetails loadUserByEmail(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email); 
        if (user == null) throw new UsernameNotFoundException("User not found");
        return new UserAuthorizationDetails(user);
    }
    
}
