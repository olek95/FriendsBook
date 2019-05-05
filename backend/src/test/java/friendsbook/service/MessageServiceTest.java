package friendsbook.service;

import friendsbook.config.WebConfiguration;
import friendsbook.domain.conversation.Message;
import friendsbook.domain.user.Gender;
import friendsbook.domain.user.User;
import friendsbook.web.MessageResource;
import friendsbook.web.UserResource;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { WebConfiguration.class })
@WebAppConfiguration
@Transactional
public class MessageServiceTest {
    @Autowired
    private MessageService messageService;
    @Autowired
    private UserService userService;
    @Autowired
    private EntityManager entityManager;
    
    private User recipient, sender;
    
    @BeforeEach
    public void createUsers() {
        entityManager.createNativeQuery("ALTER TABLE user ALTER COLUMN id RESTART WITH 1").executeUpdate();
        recipient = userService.save(createUser("login1", "email1@some.com"));
        sender = userService.save(createUser("login2", "email2@some.com"));
    }
    
    @Test
    public void testSavingTwoTheSameMessages() {
        MessageResource message = createMessageResource("somecontent", recipient.getId(), sender.getId());
        Message savedMessage1 = messageService.saveMessage(message),
                savedMessage2 = messageService.saveMessage(message);
        Message testSavedMessage = createMessage(message.getContent(), recipient, sender, savedMessage1.getId()); 
        assertEquals(testSavedMessage, savedMessage1);
        testSavedMessage.setId(savedMessage2.getId());
        assertEquals(testSavedMessage, savedMessage2);
    }
    
    @Test
    public void testSavingTwoDifferentMessages() {
        MessageResource message1 = createMessageResource("somecontent1", recipient.getId(), sender.getId()),
                message2 = createMessageResource("somecontent2", recipient.getId(), sender.getId());
        Message savedMessage1 = messageService.saveMessage(message1),
                savedMessage2 = messageService.saveMessage(message2);
        Message testSavedMessage = createMessage(message1.getContent(), recipient, sender, savedMessage1.getId());
        assertEquals(testSavedMessage, savedMessage1);
        testSavedMessage.setContent(message2.getContent());
        testSavedMessage.setId(savedMessage2.getId());
        assertEquals(testSavedMessage, savedMessage2);
    }
    
    @Test
    public void testSavingMessageWhenRecipientDoesNotExist() {
        long fakeRecipientId = 3;
        MessageResource message = createMessageResource("somecontent", fakeRecipientId, sender.getId());
        Exception noRecipientEx  = assertThrows(NoSuchElementException.class, () -> {
            messageService.saveMessage(message);
        });
        assertTrue(noRecipientEx.getMessage().contains(fakeRecipientId + ""));
    }
    
    @Test
    public void testLoadingSavedConversation() {
        MessageResource message1 = createMessageResource("somecontent1", recipient.getId(), sender.getId()),
                message2 = createMessageResource("somecontent2", recipient.getId(), sender.getId());
        Message savedMessage1 = messageService.saveMessage(message1),
                savedMessage2 = messageService.saveMessage(message2);
        List<Message> savedMessages = messageService.getConversationWithUser(sender.getId(), recipient.getId());
        List<Message> testSavedMessages = Arrays.asList(createMessage(message1.getContent(), recipient, sender, savedMessage1.getId()),
                createMessage(message2.getContent(), recipient, sender, savedMessage2.getId()));
        assertEquals(testSavedMessages, savedMessages);
    }
    
    @Test
    public void testLoadingEmptyConversation() {
        assertTrue(messageService.getConversationWithUser(sender.getId(), recipient.getId()).isEmpty());
    }
    
    @Test
    public void testLoadingConversationForCorrespondentWhichDoesNotExist() {
        assertTrue(messageService.getConversationWithUser(sender.getId(), 3L).isEmpty());
    }
    
    private UserResource createUser(String login, String email) {
        UserResource user = new UserResource();
        user.setLogin(login);
        user.setEmail(email);
        user.setBirthDate(new Date());
        user.setGender(Gender.FEMALE);
        user.setName("name");
        user.setSurname("surname");
        user.setPassword("password");
        return user;
    }
    
    private MessageResource createMessageResource(String content, Long recipientId, Long senderId) {
        MessageResource message = new MessageResource();
        message.setContent(content);
        message.setRecipientId(recipientId);
        message.setSenderId(senderId);
        return message;
    }
    
    private Message createMessage(String content, User recipient, User sender, long id) {
        Message message = new Message();
        message.setContent(content);
        message.setId(id);
        message.setRecipient(recipient);
        message.setSender(sender);
        return message;
    }
}
