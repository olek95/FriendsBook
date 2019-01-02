package friendsbook.service;

import friendsbook.dao.UserRepository;
import friendsbook.domain.User;
import friendsbook.domain.UserAuthorizationDetails;
import friendsbook.exception.DuplicatedUserException;
import friendsbook.web.UserResource;
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
        String[] emailParts = email.split("@");
        User user = userRepository.findByEmail(emailParts[0] + "@" + emailParts[1].toLowerCase());
        return user == null ? null : new UserAuthorizationDetails(user);
    }
    
    public User save(UserResource userResource) {
        User user = new User(); 
        user.setLogin(userResource.getLogin().trim().toLowerCase());
        String[] emailParts = userResource.getEmail().trim().split("@");
        user.setEmail(emailParts[0] + "@" + emailParts[1].toLowerCase());
        if (loadUserByUsername(user.getLogin()) != null || loadUserByEmail(user.getEmail()) != null) {
            throw new DuplicatedUserException();
        }
        user.setBirthDate(userResource.getBirthDate());
        user.setPassword(passwordEncoder.encode(userResource.getPassword()));
        user.setGender(userResource.getGender());
        user.setName(userResource.getName());
        user.setSurname(userResource.getSurname());
        return userRepository.save(user);
    }
}
