package service;

import friendsbook.config.WebConfiguration;
import friendsbook.domain.Gender;
import friendsbook.domain.User;
import friendsbook.domain.UserAuthorizationDetails;
import friendsbook.service.AuthenticationServiceImpl;
import friendsbook.service.UserService;
import friendsbook.web.UserResource;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import javax.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.security.authentication.BadCredentialsException;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeAll;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {WebConfiguration.class})
@WebAppConfiguration
@Transactional
public class AuthenticationServiceTest {
    @Autowired
    private AuthenticationProvider authenticationProvider;
    @Autowired
    private UserService userService;
    
    private boolean dbFilled = false;
    private static User testUser;
    
    
    @BeforeAll
    public static void createTestUser() {
        testUser = new User();
        testUser.setBirthDate(new Date());
        testUser.setEmail("sample@mail.mail");
        testUser.setGender(Gender.FEMALE);
        testUser.setLogin("Login");
        testUser.setName("Name");
        testUser.setSurname("Surname");
    }
    
    @BeforeEach
    public void addSampleUser() {
        if (!dbFilled) {
            UserResource user = new UserResource();
            user.setBirthDate(new Date());
            user.setEmail("sample@mail.mail");
            user.setGender(Gender.FEMALE);
            user.setLogin("Login");
            user.setName("Name");
            user.setPassword("Password");
            user.setSurname("Surname");
            User savedUser = userService.save(user);
            dbFilled = true;
            testUser.setId(savedUser.getId());
            testUser.setPassword(savedUser.getPassword());
        }
    }
    
    @Test
    public void testAuthenticationByLogin() throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Method retrieveUser = AuthenticationServiceImpl.class.getDeclaredMethod("retrieveUser", String.class, UsernamePasswordAuthenticationToken.class);
        retrieveUser.setAccessible(true);
        assertEquals(new UserAuthorizationDetails(testUser), retrieveUser.invoke(authenticationProvider, "Login",
                new UsernamePasswordAuthenticationToken("Login", "Password")), "Not correct user returned");
    }
    
    @Test
    public void testAuthenticationByEmail() throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Method retrieveUser = AuthenticationServiceImpl.class.getDeclaredMethod("retrieveUser", String.class, UsernamePasswordAuthenticationToken.class);
        retrieveUser.setAccessible(true);
        assertEquals(new UserAuthorizationDetails(testUser), retrieveUser.invoke(authenticationProvider, "sample@mail.mail",
                new UsernamePasswordAuthenticationToken("sample@mail.mail", "Password")), "Not correct user returned");
    }
    
    @Test
    public void testAuthenticationWithBadUsername() throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Method retrieveUser = AuthenticationServiceImpl.class.getDeclaredMethod("retrieveUser", String.class, UsernamePasswordAuthenticationToken.class);
        retrieveUser.setAccessible(true);
        Throwable cause = null;
        try {
            retrieveUser.invoke(authenticationProvider, "username", new UsernamePasswordAuthenticationToken("username", "Password"));
        } catch(InvocationTargetException ex) {
            cause = ex.getCause();
        }
        assertTrue(cause instanceof BadCredentialsException);
    }
    
    @Test
    public void testAuthenticationWithBadEmail() throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Method retrieveUser = AuthenticationServiceImpl.class.getDeclaredMethod("retrieveUser", String.class, UsernamePasswordAuthenticationToken.class);
        retrieveUser.setAccessible(true);
        Throwable cause = null;
        try {
            retrieveUser.invoke(authenticationProvider, "mail@mail.mail", new UsernamePasswordAuthenticationToken("mail@mail.mail", "Password"));
        } catch(InvocationTargetException ex) {
            cause = ex.getCause();
        }
        assertTrue(cause instanceof BadCredentialsException);
    }
    
    @Test
    public void testAuthenticationWithBadPassword() throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Method retrieveUser = AuthenticationServiceImpl.class.getDeclaredMethod("retrieveUser", String.class, UsernamePasswordAuthenticationToken.class);
        retrieveUser.setAccessible(true);
        Throwable cause = null;
        try {
            retrieveUser.invoke(authenticationProvider, "Login", new UsernamePasswordAuthenticationToken("Login", "Password123"));
        } catch(InvocationTargetException ex) {
            cause = ex.getCause();
        }
        assertTrue(cause instanceof BadCredentialsException);
    }
}
