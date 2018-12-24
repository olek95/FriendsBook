package filter;

import friendsbook.config.WebConfiguration;
import friendsbook.domain.Gender;
import friendsbook.domain.User;
import friendsbook.filter.JWTAuthenticationFilter;
import friendsbook.service.TokenAuthenticationService;
import friendsbook.service.UserService;
import java.io.IOException;
import java.util.Date;
import javax.servlet.ServletException;
import javax.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {WebConfiguration.class})
@WebAppConfiguration
@Transactional
public class JWTAuthenticationFilterTest {
    @Autowired
    private UserService userService;
    
    private boolean dbFilled;
    
    @BeforeEach
    public void addSampleUser() {
        if (!dbFilled) {
            User user = new User();
            user.setBirthDate(new Date());
            user.setEmail("sample@mail.mail");
            user.setGender(Gender.FEMALE);
            user.setLogin("Login");
            user.setName("Name");
            user.setPassword("Password");
            user.setSurname("Surname");
            userService.save(user);
        }
        dbFilled = true;
    }
    
    @Test
    public void testAuthorizedRequestPassedSuccessfully() throws IOException, ServletException {
        MockHttpServletResponse response = new MockHttpServletResponse(); 
        TokenAuthenticationService.addAuthentication(response, "Login");
        MockHttpServletRequest request = new MockHttpServletRequest(); 
        request.addHeader(HttpHeaders.AUTHORIZATION, response.getHeader(HttpHeaders.AUTHORIZATION));
        MockHttpServletResponse response2 = new MockHttpServletResponse();
        JWTAuthenticationFilter filter = new JWTAuthenticationFilter(); 
        filter.doFilter(request, response2, new MockFilterChain());
        assertEquals(HttpStatus.OK.value(), response2.getStatus());
    } 
    
    @Test
    public void testUnauthorizedRequestPassedFailly() throws IOException, ServletException {
        MockHttpServletResponse response = new MockHttpServletResponse(); 
        JWTAuthenticationFilter filter = new JWTAuthenticationFilter(); 
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(HttpHeaders.AUTHORIZATION, "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJMb2ddbiIsImV4cCI6MTU0NjI1NzY3NX0.PM1oxNhUwPlWZYIn_ISRhnls"
                + "zezM4npDdLNSs8eG_57H0vm9cHwjRh_w7BEvvARSN6adkYgpIWAc0Fi_QsRs3Q");
        filter.doFilter(request, response, new MockFilterChain());
        assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatus());
    }
}
