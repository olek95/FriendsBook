package friendsbook.filter;

import java.util.Base64;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        String encodedCredentials = authorization.substring("Basic".length()).trim();
        byte[] decodedCredentials = Base64.getDecoder().decode(encodedCredentials);
        String credentials = new String(decodedCredentials);
        String[] data = credentials.split(":"); // name:password
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(data[0], data[1]);
        setDetails(request, token);
        return this.getAuthenticationManager().authenticate(token);
    }

}
