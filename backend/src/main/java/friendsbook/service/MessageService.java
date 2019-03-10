package friendsbook.service;

import friendsbook.dao.MessageRepository;
import friendsbook.domain.conversation.Message;
import friendsbook.web.MessageResource;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageService {
    private MessageRepository messageRepository; 
    private UserService userService; 
    
    @Autowired
    public MessageService(MessageRepository messageRepository, UserService userService) {
        this.messageRepository = messageRepository; 
        this.userService = userService; 
    }
    
    public void saveMessage(MessageResource messageResource) {
        Message message = new Message(); 
        message.setContent(messageResource.getContent());
        message.setSender(userService.getUser(messageResource.getSenderId()));
        message.setRecipient(userService.getUser(messageResource.getRecipientId()));
        messageRepository.save(message);
    }
    
    public List<Message> getConversationWithUser(long userId, long correspondentId) {
        return this.messageRepository.findConversation(userId, correspondentId);
    }
}
