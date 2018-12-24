package service;

import friendsbook.config.WebConfiguration;
import friendsbook.domain.Gender;
import friendsbook.domain.User;
import friendsbook.domain.UserAuthorizationDetails;
import friendsbook.exception.DuplicatedUserException;
import friendsbook.service.UserService;
import java.util.Arrays;
import java.util.Date;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
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
    
    private User user; 
    
    @BeforeEach
    public void createUser() {
        user = new User();
        user.setBirthDate(new Date());
        user.setEmail("sample@mail.mail");
        user.setGender(Gender.FEMALE);
        user.setLogin("Login");
        user.setName("Name");
        user.setPassword("Password");
        user.setSurname("Surname");
    }
    
    @Test
    public void testRegistration() {
        User savedUser = userService.save(user);
        assertEquals(user, savedUser, "Saved user is different from original user");
    }
    
    @Test
    public void testRegistrationForDuplicatedUser() {
        userService.save(user);
        assertThrows(DuplicatedUserException.class, () -> {
            userService.save(user);
        }, "User duplication not noticed");
    }
    
    @Test
    public void testNotAllowedRegistrationWhenSomeFieldsAreNull() {
        User userWithMissingValues = new User();
        userWithMissingValues.setPassword("Password");
        ConstraintViolationException ex = assertThrows(ConstraintViolationException.class, () -> {
            userService.save(userWithMissingValues);
        });
        String[] expectedViolationProperties = new String[] {"email", "gender", "login", "name", "surname", "birthDate"},
                violationProperties = new String[expectedViolationProperties.length];
        int i = 0; 
        for (ConstraintViolation constraint : ex.getConstraintViolations()) {
            violationProperties[i++] = constraint.getPropertyPath().toString();
        }
        Arrays.sort(expectedViolationProperties);
        Arrays.sort(violationProperties);
        assertArrayEquals(expectedViolationProperties, violationProperties);
    }
    
    @Test
    public void testNotAllowedRegistrationWhenEmailHasOnlyUserId() {
        user.setEmail("email");
        assertThrows(ConstraintViolationException.class, () -> {
            userService.save(user);    
        });
    }
    
    @Test
    public void testNotAllowedRegistrationWhenEmailHasUserIdAndAtSign() {
        user.setEmail("email@");
        assertThrows(ConstraintViolationException.class, () -> {
            userService.save(user);    
        });
    }
    
    @Test
    public void testNotAllowedRegistrationWhenEmailHasTrailingDotSign() {
        user.setEmail("email@sample.sample.");
        assertThrows(ConstraintViolationException.class, () -> {
            userService.save(user);    
        });
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
        createUser();
        user.setId(savedUser.getId());
        user.setBirthDate(savedUser.getBirthDate());
        user.setPassword(savedUser.getPassword());
        assertEquals(new UserAuthorizationDetails(user), userService.loadUserByUsername(savedUser.getLogin()));
    }
    
    @Test
    public void testLoadingUserByEmail() {
        User savedUser = userService.save(user);
        createUser();
        user.setId(savedUser.getId());
        user.setBirthDate(savedUser.getBirthDate());
        user.setPassword(savedUser.getPassword());
        assertEquals(new UserAuthorizationDetails(user), userService.loadUserByEmail(savedUser.getEmail()));
    }
}
