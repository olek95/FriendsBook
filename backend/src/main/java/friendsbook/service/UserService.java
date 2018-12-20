package friendsbook.service;

import friendsbook.dao.UserRepository;
import friendsbook.domain.User;
import friendsbook.domain.UserAuthorizationDetails;
import javax.validation.Valid;
import org.hibernate.validator.constraints.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository; 
    private final PasswordEncoder passwordEncoder;
    
    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository; 
        this.passwordEncoder = passwordEncoder;
    }
    
    @Override
    public UserDetails loadUserByUsername(String login) {
        User user = userRepository.findByLogin(login); 
        return user == null ? null : new UserAuthorizationDetails(user);
    }
    
    public UserDetails loadUserByEmail(@Valid @Email String email) {
        User user = userRepository.findByEmail(email); 
        return user == null ? null : new UserAuthorizationDetails(user);
    }
    
    public User save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }
}
