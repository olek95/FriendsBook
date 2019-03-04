package friendsbook.web;

import friendsbook.domain.user.User;
import friendsbook.service.UserService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChatContactsController {
    private UserService userService; 
    
    @Autowired
    public ChatContactsController(UserService userService) {
        this.userService = userService;
    }
    @Autowired
    private SimpMessagingTemplate t;
    
    @GetMapping("/chat-contacts")
    public ChatContactResource[] getAllChatContacts(@RequestParam long userId) {
        List<User> users = userService.getChatContactsForSpecificUser(userId);
        int contactsNumber = users.size();
        ChatContactResource[] contacts = new ChatContactResource[contactsNumber];
        for (int i = 0; i < contactsNumber; i++) {
            ChatContactResource contact = new ChatContactResource();
            User user = users.get(i);
            contact.setId(user.getId());
            contact.setName(user.getName());
            contact.setSurname(user.getSurname());
            contact.setLogin(user.getLogin());
            contacts[i] = contact;
        }
        return contacts;
    }
    
    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public String greetings() throws Exception {
        return "ABC";
    }
    
}
