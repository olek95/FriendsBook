package friendsbook.service;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockHttpServletResponse;

@Configuration
@Profile("test")
public class MockUserService {
    public static final String JWT_TOKEN_AUTHORIZATION = resolveToken();
    public static final String ACTUAL_AUTHENTICATED_TEST_USER = "login2";
    public static final long ACTUAL_AUTHENTICATED_TEST_USER_ID = 2;
    
    private static String resolveToken() {
        MockHttpServletResponse response = new MockHttpServletResponse();
        TokenAuthenticationService.addAuthentication(response, ACTUAL_AUTHENTICATED_TEST_USER, ACTUAL_AUTHENTICATED_TEST_USER_ID);
        return response.getHeader(HttpHeaders.AUTHORIZATION);
    }
}
