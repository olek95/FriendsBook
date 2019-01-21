package service;

import friendsbook.config.WebConfiguration;
import friendsbook.service.TokenAuthenticationService;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import java.util.ArrayList;
import javax.transaction.Transactional;
import javax.ws.rs.core.HttpHeaders;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {WebConfiguration.class})
@WebAppConfiguration
@Transactional
public class TokenAuthenticationServiceTest {
    @Test
    public void testGetAuthenticationWithUsingJWTToken() {
        MockHttpServletResponse response = new MockHttpServletResponse(); 
        TokenAuthenticationService.addAuthentication(response, "Login");
        assertTrue(response.containsHeader(HttpHeaders.AUTHORIZATION));
        assertEquals(new UsernamePasswordAuthenticationToken("Login", null, new ArrayList()),
                TokenAuthenticationService.getAuthentication(response.getHeader(HttpHeaders.AUTHORIZATION)));
    }
    
    @Test
    public void testGetAuthenticationWithBadJWTTokenConstruction() {
        assertThrows(MalformedJwtException.class, () -> {
            TokenAuthenticationService.getAuthentication("TOKEN");
        });
    }
    
    @Test
    public void testGetAuthenticationWithCorrectJWTTokenForUserWhoNotExists() {
        assertThrows(SignatureException.class, () -> {
            TokenAuthenticationService.getAuthentication("Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJMb2ddbiIsImV4cCI6MTU0NjI1NzY3NX0.PM1oxNhUwPlWZYIn_ISRhnls"
                + "zezM4npDdLNSs8eG_57H0vm9cHwjRh_w7BEvvARSN6adkYgpIWAc0Fi_QsRs3Q");
        });
    }
}
