package friendsbook.web;

import friendsbook.domain.User;
import friendsbook.service.UserService;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController {

    private final UserService userService;

    @Autowired
    public AuthenticationController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/account/register")
    public ResponseEntity<String> register(@Valid @RequestBody User user) {
        if (this.userService.loadUserByUsername(user.getLogin()) != null || this.userService.loadUserByEmail(user.getEmail()) != null) {
            return new ResponseEntity<>("User already exists", HttpStatus.CONFLICT);
        } else {
            this.userService.save(user);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
    }
}
