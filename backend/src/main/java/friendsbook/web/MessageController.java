package friendsbook.web;

import friendsbook.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MessageController {
    private MessageService messageService; 
    private SimpMessagingTemplate messagingTemplate;
    
    @Autowired
    public MessageController(MessageService messageService, SimpMessagingTemplate messagingTemplate) {
        this.messageService = messageService; 
        this.messagingTemplate = messagingTemplate;
    }
    
    @MessageMapping("/message/{username}")
    public void sendMessage(MessageResource message, @DestinationVariable String username) {
        messageService.saveMessage(message);
        messagingTemplate.convertAndSendToUser(username, "/queue/message", message);
    }
}
