package friendsbook.service;

import friendsbook.config.WebConfiguration;
import friendsbook.domain.user.Gender;
import friendsbook.domain.user.User;
import friendsbook.model.UserDetailsImpl;
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

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {WebConfiguration.class})
@WebAppConfiguration
@Transactional
public class AuthenticationServiceTest {
    @Autowired
    private AuthenticationProvider authenticationProvider;
    @Autowired
    private UserService userService;
    
    private static User testUser;
    
    
    @BeforeEach
    public void createTestUser() {
        testUser = new User();
        testUser.setBirthDate(new Date());
        testUser.setEmail("sample@mail.mail");
        testUser.setGender(Gender.FEMALE);
        testUser.setLogin("Login");
        testUser.setName("Name");
        testUser.setSurname("Surname");
        testUser.setPassword("Password");
    }
    
    @BeforeEach
    public void addSampleUser() {
        UserResource user = new UserResource();
        user.setBirthDate(new Date());
        user.setEmail("sample@mail.mail");
        user.setGender(Gender.FEMALE);
        user.setLogin("Login");
        user.setName("Name");
        user.setPassword("Password");
        user.setSurname("Surname");
        userService.save(user);
    }
    
    @Test
    public void testAuthenticationByLogin() throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Method retrieveUser = AuthenticationServiceImpl.class.getDeclaredMethod("retrieveUser", String.class, UsernamePasswordAuthenticationToken.class);
        retrieveUser.setAccessible(true);
        assertEquals(new UserDetailsImpl(testUser), retrieveUser.invoke(authenticationProvider, "Login",
                new UsernamePasswordAuthenticationToken("Login", "Password")), "Not correct user returned");
    }
    
    @Test
    public void testAuthenticationByEmail() throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Method retrieveUser = AuthenticationServiceImpl.class.getDeclaredMethod("retrieveUser", String.class, UsernamePasswordAuthenticationToken.class);
        retrieveUser.setAccessible(true);
        assertEquals(new UserDetailsImpl(testUser), retrieveUser.invoke(authenticationProvider, "sample@mail.mail",
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
