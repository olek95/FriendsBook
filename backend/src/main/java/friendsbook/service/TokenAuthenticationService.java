package friendsbook.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Collections;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

public class TokenAuthenticationService {
    private static final String TOKEN_PREFIX = "Bearer";
    
    public static void addAuthentication(HttpServletResponse response, String username) {
       response.addHeader(HttpHeaders.AUTHORIZATION, TOKEN_PREFIX + " " +
               Jwts.builder().setSubject(username)
               .setExpiration(new Date(System.currentTimeMillis() + 604800000)) // 1 week
               .signWith(SignatureAlgorithm.HS512, "Secret").compact());
    }
    
    public static Authentication getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (token != null) {
            String username = Jwts.parser().setSigningKey("Secret")
                    .parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
                    .getBody().getSubject();
            return username != null ? new UsernamePasswordAuthenticationToken(username, null, Collections.emptyList())
                    : null;
        }
        return null;
    }
}
