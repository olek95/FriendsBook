package friendsbook.web;

import friendsbook.domain.User;
import friendsbook.service.UserService;
import org.apache.commons.validator.ValidatorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController { 
    private final UserService userService;
    
    @Autowired
    public AuthenticationController(UserService userService) {
        this.userService = userService;
    }
    
    @PostMapping("/account/register")
    public ResponseEntity<String> register(@RequestBody User user) throws ValidatorException {
        if(this.userService.loadUserByUsername(user.getLogin()) != null || this.userService.loadUserByEmail(user.getEmail()) != null) {
            return new ResponseEntity<>("User already exists", HttpStatus.CONFLICT);
        } else {
            this.userService.save(user);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
    }
    
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler({ ValidatorException.class })
    public String handleException() {
        return "Bad email format";
    }
}