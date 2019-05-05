package friendsbook.service;

import friendsbook.dao.MessageRepository;
import friendsbook.domain.conversation.Message;
import friendsbook.domain.user.User;
import friendsbook.web.MessageResource;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageService {
    private final MessageRepository messageRepository; 
    private final UserService userService; 
    
    @Autowired
    public MessageService(MessageRepository messageRepository, UserService userService) {
        this.messageRepository = messageRepository; 
        this.userService = userService; 
    }
    
    public Message saveMessage(MessageResource messageResource) {
        Message message = new Message(); 
        message.setContent(messageResource.getContent());
        message.setSender(userService.getUser(messageResource.getSenderId()));
        long recipientId = messageResource.getRecipientId();
        User recipient = userService.getUser(recipientId);
        if (recipient == null) {
            throw new NoSuchElementException("Recipient with " + recipientId + " id doesn't exist");
        }
        message.setRecipient(recipient);
        messageRepository.save(message);
        return message;
    }
    
    public List<Message> getConversationWithUser(long userId, long correspondentId) {
        return this.messageRepository.findConversation(userId, correspondentId);
    }
}
