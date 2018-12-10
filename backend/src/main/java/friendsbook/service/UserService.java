package friendsbook.service;

import friendsbook.dao.UserRepository;
import friendsbook.domain.User;
import friendsbook.domain.UserAuthorizationDetails;
import org.apache.commons.validator.EmailValidator;
import org.apache.commons.validator.ValidatorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {
    Logger logger = LoggerFactory.getLogger(UserService.class.toString());
    private final UserRepository userRepository; 
    
    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository; 
    }
    
    @Override
    public UserDetails loadUserByUsername(String login) {
        logger.info("MOJ LOGIN: " + login);
        User user = userRepository.findByLogin(login); 
        return user == null ? null : new UserAuthorizationDetails(user);
    }
    
    public UserDetails loadUserByEmail(String email) {
        User user = userRepository.findByEmail(email); 
        return user == null ? null : new UserAuthorizationDetails(user);
    }
    
    public User save(User user) throws ValidatorException {
        if (EmailValidator.getInstance().isValid(user.getEmail())) {
            return userRepository.save(user);
        } else {
            throw new ValidatorException();
        }
    }
}
