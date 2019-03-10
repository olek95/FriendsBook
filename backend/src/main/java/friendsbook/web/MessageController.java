package friendsbook.web;

import friendsbook.domain.conversation.Message;
import friendsbook.model.UserAuthentication;
import friendsbook.service.MessageService;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    public void sendMessage(MessageResource message, @DestinationVariable String username, Principal principal) {
        message.setSenderId(((UserAuthentication)principal).getId());
        messageService.saveMessage(message);
        messagingTemplate.convertAndSendToUser(username, "/queue/message", message);
    }
    
    @GetMapping("/message/{correspondentId}")
    public List<MessageResource> getConversationWithUser(@PathVariable long correspondentId, Principal principal) {
        List<Message> messages = this.messageService.getConversationWithUser(((UserAuthentication)principal).getId(), correspondentId);
        List<MessageResource> messagesResource = new ArrayList<>();
        for (Message message : messages) {
            MessageResource messageResource = new MessageResource();
            messageResource.setContent(message.getContent());
            messageResource.setRecipientId(message.getRecipient().getId());
            messageResource.setSenderId(message.getSender().getId());
            messagesResource.add(messageResource);
        }
        return messagesResource;
    }
}
