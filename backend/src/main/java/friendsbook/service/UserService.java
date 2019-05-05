package friendsbook.service;

import friendsbook.dao.UserRepository;
import friendsbook.domain.user.User;
import friendsbook.model.UserDetailsImpl;
import friendsbook.exception.DuplicatedUserException;
import friendsbook.web.UserResource;
import java.util.List;
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
        return user == null ? null : new UserDetailsImpl(user);
    }
    
    /**
     * Loads user by email by comparing email of each user to passed email. 
     * This comparison need to be done manually as we need case sensitive
     * and not with using BINARY attribute for MySQL column as it seems like
     * it is not supported for H2 db used for tests.
     * @param email
     * @return user which has the same email (case sensitive)
     */
    public UserDetails loadUserByEmail(@Valid @Email String email) {
        String[] emailParts = email.split("@");
        List<User> users = userRepository.findByEmail(emailParts[0] + "@" + emailParts[1].toLowerCase());
        User user = null;
        for (User storedUser : users) {
            String emailUsernamePart = storedUser.getEmail().split("@")[0];
            if (emailUsernamePart.equals(emailParts[0])) {
                user = storedUser;
            }
        }
        return user == null ? null : new UserDetailsImpl(user);
    }
    
    public User getUser(long id) {
        return userRepository.findById(id).orElse(null);
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
    
    public List<User> getChatContactsForSpecificUser(long id) {
        User user = userRepository.findById(id).get();
        List<User> friends = user.getFriends();
        friends.addAll(user.getFriendsOf());
        return friends;
    }
}
