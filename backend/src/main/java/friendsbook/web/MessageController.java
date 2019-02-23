package friendsbook.web;

import friendsbook.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MessageController {
    private MessageService messageService; 
    
    @Autowired
    public MessageController(MessageService messageService) {
        this.messageService = messageService; 
    }
    
    @MessageMapping("/message")
    @SendTo("/topic/message")
    public String sendMessage(MessageResource message) {
        messageService.saveMessage(message);
        return message.getContent();
    }
    
}
