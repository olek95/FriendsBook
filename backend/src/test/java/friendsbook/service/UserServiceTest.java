package friendsbook.service;

import friendsbook.config.WebConfiguration;
import friendsbook.domain.user.Gender;
import friendsbook.domain.user.User;
import friendsbook.model.UserDetailsImpl;
import friendsbook.exception.DuplicatedUserException;
import friendsbook.web.UserResource;
import java.util.Date;
import javax.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {WebConfiguration.class})
@WebAppConfiguration
@Transactional
public class UserServiceTest {
    @Autowired
    private UserService userService;
    
    private UserResource user; 
    private User testSavedUser;
    
    @BeforeEach
    public void createUser() {
        user = new UserResource();
        user.setBirthDate(new Date());
        user.setEmail("sample@mail.mail");
        user.setGender(Gender.FEMALE);
        user.setLogin("Login");
        user.setName("Name");
        user.setPassword("Password");
        user.setSurname("Surname");
    }
    
    @BeforeEach
    public void createTestSavedUser() {
        testSavedUser = new User();
        testSavedUser.setBirthDate(new Date());
        testSavedUser.setEmail("sample@mail.mail");
        testSavedUser.setGender(Gender.FEMALE);
        testSavedUser.setLogin("Login");
        testSavedUser.setName("Name");
        testSavedUser.setSurname("Surname");
        testSavedUser.setPassword("Password");
    }
    
    @Test
    public void testRegistration() {
        User savedUser = userService.save(user);
        assertEquals(testSavedUser, savedUser, "Saved user is different from original user");
    }
    
    @Test
    public void testRegistrationForDuplicatedUser() {
        userService.save(user);
        assertThrows(DuplicatedUserException.class, () -> {
            userService.save(user);
        }, "User duplication not noticed");
    }
    
    @Test
    public void testRegistrationForDuplicatedUserWithWhiteSpacesInUsername() {
        userService.save(user);
        createTestSavedUser();
        user.setEmail("newmail@newmail.newmail");
        user.setLogin("    " + user.getLogin() + "     ");
        assertThrows(DuplicatedUserException.class, () -> {
            userService.save(user);
        });
    }
    
    @Test
    public void testRegistrationForDuplicatedUserWithWhiteSpacesInEmail() {
        userService.save(user);
        createTestSavedUser();
        user.setLogin("NewLogin");
        user.setEmail("     " + user.getEmail() + "     ");
        assertThrows(DuplicatedUserException.class, () -> {
            userService.save(user);
        });
    }
    
    @Test
    public void testRegistrationForDuplicatedUserWithUsernameCaseInsensitive() {
        userService.save(user);
        createTestSavedUser(); 
        user.setLogin(user.getLogin().toUpperCase());
        user.setEmail("newmail@mail.mail");
        assertThrows(DuplicatedUserException.class, () -> {
            userService.save(user);
        });
    }
    
    @Test
    public void testRegistrationForDuplicatedUserWithEmailDomainCaseInsensitive() {
        userService.save(user);
        createTestSavedUser(); 
        user.setLogin("Login1");
        String[] emailParts = user.getEmail().split("@");
        user.setEmail(emailParts[0] + "@" + emailParts[1].toUpperCase());
        assertThrows(DuplicatedUserException.class, () -> {
            userService.save(user);
        });
    }
    
    @Test 
    public void testRegistrationForUserWithEmailUserIdCaseSensitive() {
        userService.save(user);
        createTestSavedUser(); 
        user.setLogin("Login1");
        String[] emailParts = user.getEmail().split("@");
        user.setEmail(emailParts[0].toUpperCase() + "@" + emailParts[1]);
        userService.save(user);
    }
    
    @Test
    public void testLoadingUserByLoginWhenUserNotExists() {
        assertNull(userService.loadUserByUsername("Login"));
    }
    
    @Test
    public void testLoadingUserByEmailWhenUserNotExists() {
        assertNull(userService.loadUserByEmail("email@email.email"));
    }
    
    @Test
    public void testLoadingUserByLogin() {
        User savedUser = userService.save(user); 
        assertEquals(new UserDetailsImpl(testSavedUser), userService.loadUserByUsername(savedUser.getLogin()));
    }
    
    @Test
    public void testLoadingUserByLoginForCaseInsensitive() {
        User savedUser = userService.save(user); 
        assertEquals(new UserDetailsImpl(testSavedUser), userService.loadUserByUsername(savedUser.getLogin().toUpperCase()));
    }
    
    @Test
    public void testLoadingUserByEmail() {
        User savedUser = userService.save(user);
        assertEquals(new UserDetailsImpl(testSavedUser), userService.loadUserByEmail(savedUser.getEmail()));
    }
    
    @Test
    public void testLoadingUserByEmailForDomainPartCaseInsensitive() {
        User savedUser = userService.save(user);
        String[] emailParts = savedUser.getEmail().split("@");
        assertEquals(new UserDetailsImpl(testSavedUser), userService.loadUserByEmail(emailParts[0] + "@" + emailParts[1].toUpperCase()));
    }
    
    @Test
    public void testLoadingUserByEmailForUserIdPartCaseSensitiveWhenUserNotExists() {
        String[] emailParts = user.getEmail().split("@");
        assertNull(userService.loadUserByEmail(emailParts[0].toUpperCase() + "@" + emailParts[1]));
    }
}
