package friendsbook.filter;

import friendsbook.service.TokenAuthenticationService;
import java.io.IOException;
import java.util.Base64;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authorization != null && authorization.startsWith("Basic ")) {
            String encodedCredentials = authorization.substring("Basic".length()).trim();
            byte[] decodedCredentials = Base64.getDecoder().decode(encodedCredentials);
            String credentials = new String(decodedCredentials);
            String[] data = credentials.split(":"); // name:password
            if (data.length == 2) {
                UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(data[0], data[1]);
                setDetails(request, token);
                return this.getAuthenticationManager().authenticate(token);
            }
        }
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        return null;
    }
    
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
            FilterChain chain, Authentication authentication) throws IOException, ServletException {
        TokenAuthenticationService.addAuthentication(response, authentication.getName());
        response.setHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.AUTHORIZATION);
    }
}
