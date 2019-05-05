package friendsbook.service;

import friendsbook.model.UserAuthentication;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;

public class TokenAuthenticationService {
    private static final String TOKEN_PREFIX = "Bearer";
    private static final String USER_ID_KEY = "userId";
    private static final String SECRET_VALUE = "Secret";
    
    public static void addAuthentication(HttpServletResponse response, String username, long userId) {
        response.addHeader(HttpHeaders.AUTHORIZATION, TOKEN_PREFIX + " " +
               Jwts.builder().setSubject(username).claim(USER_ID_KEY, userId)
                        .setExpiration(new Date(System.currentTimeMillis() + 604800000)) // 1 week
                        .signWith(SignatureAlgorithm.HS512, SECRET_VALUE).compact());
    }
    
    public static Authentication getAuthentication(String token) {
        if (token != null) {
            Claims body = Jwts.parser().setSigningKey(SECRET_VALUE)
                    .parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
                    .getBody();
            String username = body.getSubject();
            if (username != null) {
                Authentication user = new UserAuthentication(username, null);
                ((UserAuthentication)user).setId(new Long(body.get(USER_ID_KEY).toString()));
                return user;
            }
            return null;
        }
        return null;
    }
}
