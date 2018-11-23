package friendsbook.web;

import java.security.Principal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController {
    
    @GetMapping("/account/login")
    public Principal user(Principal user) {
        return user;
    }
}
