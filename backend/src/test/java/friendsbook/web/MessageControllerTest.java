package friendsbook.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import friendsbook.config.WebConfiguration;
import friendsbook.domain.user.Gender;
import friendsbook.domain.user.User;
import friendsbook.model.UserAuthentication;
import friendsbook.service.MessageService;
import friendsbook.service.MockUserService;
import friendsbook.service.UserService;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.transaction.Transactional;
import javax.ws.rs.core.HttpHeaders;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.AbstractSubscribableChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;

@ExtendWith({SpringExtension.class} )
@ContextConfiguration(classes = { WebConfiguration.class })
@WebAppConfiguration
@ActiveProfiles("test")
public class MessageControllerTest {
    @Autowired
    private EntityManagerFactory entityManagerFactory;
    @Autowired
    private UserService userService;
    @Autowired
    private MessageService messageService;
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private AbstractSubscribableChannel brokerChannel;
    @Autowired
    private AbstractSubscribableChannel clientOutboundChannel;
    @Autowired
    private AbstractSubscribableChannel clientInboundChannel;
    
    private User recipient, sender;
    private static MockMvc mvc;
    private TestChannelInterceptor brokerChannelInterceptor, clientOutboundChannelInterceptor;
    
    @BeforeEach
    public void createUsers() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.createNativeQuery("ALTER TABLE user ALTER COLUMN id RESTART WITH 1").executeUpdate();
        entityManager.createNativeQuery("DELETE FROM message").executeUpdate();
        entityManager.createNativeQuery("DELETE FROM user").executeUpdate();
        transaction.commit();
        recipient = userService.save(createUser("login1", "email1@some.com"));
        sender = userService.save(createUser("login2", "email2@some.com"));
    }
    
    @BeforeEach
    public void createMvc() {
        if (mvc == null) {
            mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).apply(SecurityMockMvcConfigurers.springSecurity()).build();
        }
    }
    
    @BeforeEach
    public void setup() {
        brokerChannelInterceptor = new TestChannelInterceptor();
        clientOutboundChannelInterceptor = new TestChannelInterceptor();
        brokerChannel.addInterceptor(brokerChannelInterceptor);
        clientOutboundChannel.addInterceptor(clientOutboundChannelInterceptor);
    }
    
    @Test
    @Transactional
    public void testGettingConversation() throws Exception {
        MessageResource message1 = createMessageResource("content1", recipient.getId(), sender.getId()),
                message2 = createMessageResource("content2", recipient.getId(), sender.getId());
        messageService.saveMessage(message1);
        messageService.saveMessage(message2);
        List<MessageResource> expectedConversation = Arrays.asList(message1, message2);
        mvc.perform(MockMvcRequestBuilders.get("/message/{recipientId}", recipient.getId()).header(HttpHeaders.AUTHORIZATION, MockUserService.JWT_TOKEN_AUTHORIZATION))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(new ObjectMapper().writeValueAsString(expectedConversation)));
    }
    
    @Test
    @Transactional
    public void testGettingConversationWhenUserIsNotAuthenticated() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/message/{recipientId}", recipient.getId()))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }
    
    @Test
    @Transactional
    public void testGettingEmptyConversation() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/message/{recipientId}", recipient.getId())
                .header(HttpHeaders.AUTHORIZATION, MockUserService.JWT_TOKEN_AUTHORIZATION))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("[]"));
    }
    
    @Test
    @Transactional
    public void testGettingConversationWithUserWhichDoesNotExist() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/message/{recipientId}", recipient.getId())
                .header(HttpHeaders.AUTHORIZATION, MockUserService.JWT_TOKEN_AUTHORIZATION))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("[]"));
    }
    
    @Test
    public void testShouldSaveMessage() throws InterruptedException, JsonProcessingException {
        StompHeaderAccessor headers = StompHeaderAccessor.create(StompCommand.SEND);
        headers.setSubscriptionId("0");
        headers.setDestination("/app/message/" + "user");
        headers.setSessionId(RequestContextHolder.currentRequestAttributes().getSessionId());
        UserAuthentication user = new UserAuthentication(sender.getLogin(), sender.getPassword());
        user.setId(sender.getId());
        headers.setUser(user);
        headers.setSessionAttributes(new HashMap<>());
        MessageResource payload = new MessageResource();
        payload.setContent("somecontent");
        payload.setRecipientId(recipient.getId());
        payload.setSenderId(sender.getId());
        Message<byte[]> message = MessageBuilder.createMessage(new ObjectMapper().writeValueAsString(payload).getBytes(), headers.getMessageHeaders());
        clientOutboundChannelInterceptor.setIncludedDestinations("/app/message/" + "user");
        clientInboundChannel.send(message);
        Message<?> positionUpdate = brokerChannelInterceptor.awaitMessage(5);
        assertNotNull(positionUpdate);
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
}
