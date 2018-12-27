package web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import friendsbook.config.WebConfiguration;
import friendsbook.domain.Gender;
import friendsbook.service.UserService;
import friendsbook.web.UserResource;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import javax.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {WebConfiguration.class})
@WebAppConfiguration
@Transactional
public class AuthenticationControllerTest {
    @Autowired
    private UserService userService;
    @Autowired
    private WebApplicationContext webApplicationContext;
    
    private MockMvc mvc;
    private boolean dbFilled; 
    private ObjectMapper mapper = new ObjectMapper(); 
    private UserResource testUser;
    
    @BeforeEach
    private void createTestUser() {
        testUser = new UserResource();
        testUser.setBirthDate(new Date());
        testUser.setEmail("sample@mail.mail");
        testUser.setGender(Gender.FEMALE);
        testUser.setLogin("Login");
        testUser.setName("Name");
        testUser.setPassword("Password");
        testUser.setSurname("Surname");
    }
    
    @BeforeEach
    private void setup() {
        if (!dbFilled) {
            userService.save(testUser);
        }
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).apply(SecurityMockMvcConfigurers.springSecurity()).build();
    }
    
    @Test
    public void testRegistrationNewUser() throws Exception {
        testUser.setLogin("Login1");
        testUser.setEmail("sample1@mail.mail");
        mvc.perform(createRegistrationRequest(testUser)).andExpect(MockMvcResultMatchers.status().isCreated());
    }
    
    @Test
    public void testRegistrationWithUsernameWhichExists() throws Exception {
        testUser.setEmail("sample1@mail.mail");
        mvc.perform(createRegistrationRequest(testUser)).andExpect(MockMvcResultMatchers.status().isConflict());
    }
    
    @Test
    public void testRegistrationWithEmailWhichExists() throws Exception {
        testUser.setLogin("Login1");
        mvc.perform(createRegistrationRequest(testUser)).andExpect(MockMvcResultMatchers.status().isConflict());
    }
    
    @Test
    public void testRegistrationWithNullValues() throws Exception {
        UserResource user = new UserResource(); 
        String[] expectedResultKeys = new String[] {"password", "gender", "surname", "name", "login", "birthDate", "email"};
        String resultJson = mvc.perform(createRegistrationRequest(user)).andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn().getResponse().getContentAsString();
        Object[] resultKeys = mapper.readValue(resultJson, Map.class).keySet().toArray();
        Arrays.sort(expectedResultKeys);
        Arrays.sort(resultKeys);
        assertArrayEquals(expectedResultKeys, resultKeys);
    }
    
    @Test
    public void testRegistrationWithInvalidValuesLength() throws Exception {
        UserResource user = new UserResource(); 
        user.setBirthDate(new Date());
        user.setGender(Gender.FEMALE);
        user.setLogin("LoginLoginLoginLoginLoginLoginLogin");
        user.setPassword("PasswordPasswordPasswordPasswordPasswordPasswordPasswordPassword");
        user.setEmail("sample@mail.mailmailmailmailmailmailmailmailmailmailmailmailmailmailmailmailmailmailmailmailmailmailmailmailmailmailmailmailmailmailmailmailma"
                + "ilmailmailmailmailmailmailmailmailmailmailmailmailmailmailmailmailmailmailmailmailmailmailmailmailmailmailmailmail");
        user.setName("NameNameNameNameNameNameNameName");
        user.setSurname("SurnameSurnameSurnameSurnameSurname");
        String[] expectedResultKeys = new String[] {"login", "password", "email", "name", "surname"};
        String resultJson = mvc.perform(createRegistrationRequest(user)).andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn().getResponse().getContentAsString();
        Object[] resultKeys = mapper.readValue(resultJson, Map.class).keySet().toArray();
        Arrays.sort(expectedResultKeys);
        Arrays.sort(resultKeys);
        assertArrayEquals(expectedResultKeys, resultKeys);
    }
    
    @Test
    public void testRegistrationWhenEmailHasTrailingDotSign() throws Exception {
        testUser.setEmail("mail@mail.mail.");
        testUser.setLogin("Login1");
        String[] expectedResultKeys = new String[] {"email"};
        String resultJson = mvc.perform(createRegistrationRequest(testUser)).andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn().getResponse().getContentAsString();
        Object[] resultKeys = mapper.readValue(resultJson, Map.class).keySet().toArray();
        Arrays.sort(expectedResultKeys);
        Arrays.sort(resultKeys);
        assertArrayEquals(expectedResultKeys, resultKeys);
    }
    
    @Test
    public void testRegistrationWhenEmailHasOnlyUserId() throws Exception {
        testUser.setEmail("mail");
        testUser.setLogin("Login1");
        String[] expectedResultKeys = new String[] {"email"};
        String resultJson = mvc.perform(createRegistrationRequest(testUser)).andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn().getResponse().getContentAsString();
        Object[] resultKeys = mapper.readValue(resultJson, Map.class).keySet().toArray();
        Arrays.sort(expectedResultKeys);
        Arrays.sort(resultKeys);
        assertArrayEquals(expectedResultKeys, resultKeys);
    }
    
    @Test
    public void testRegistrationWhenEmailHasUserIdAndAtSign() throws Exception {
        testUser.setEmail("mail@");
        testUser.setLogin("Login1");
        String[] expectedResultKeys = new String[] {"email"};
        String resultJson = mvc.perform(createRegistrationRequest(testUser)).andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn().getResponse().getContentAsString();
        Object[] resultKeys = mapper.readValue(resultJson, Map.class).keySet().toArray();
        Arrays.sort(expectedResultKeys);
        Arrays.sort(resultKeys);
        assertArrayEquals(expectedResultKeys, resultKeys);
    }
    
    @Test
    public void testRegistrationWithAtSignInUsername() throws Exception {
        testUser.setEmail("mail@mail.mail");
        testUser.setLogin("Login@Login");
        String[] expectedResultKeys = new String[] {"login"};
        String resultJson = mvc.perform(createRegistrationRequest(testUser)).andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn().getResponse().getContentAsString();
        Object[] resultKeys = mapper.readValue(resultJson, Map.class).keySet().toArray();
        Arrays.sort(expectedResultKeys);
        Arrays.sort(resultKeys);
        assertArrayEquals(expectedResultKeys, resultKeys);
    }
    
    private RequestBuilder createRegistrationRequest(UserResource user) throws JsonProcessingException {
        return MockMvcRequestBuilders.post("/account/register").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(user));
    }
}
