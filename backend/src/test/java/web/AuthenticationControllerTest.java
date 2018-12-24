package web;

import com.fasterxml.jackson.databind.ObjectMapper;
import friendsbook.config.WebConfiguration;
import friendsbook.domain.Gender;
import friendsbook.domain.User;
import friendsbook.service.UserService;
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
    
    @BeforeEach
    private void setup() {
        if (!dbFilled) {
            userService.save(createUser());
        }
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).apply(SecurityMockMvcConfigurers.springSecurity()).build();
    }
    
    @Test
    public void testRegistrationNewUser() throws Exception {
        User user = createUser();
        user.setLogin("Login1");
        user.setEmail("sample1@mail.mail");
        RequestBuilder request = MockMvcRequestBuilders.post("/account/register").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(user));
        mvc.perform(request).andExpect(MockMvcResultMatchers.status().isCreated());
    }
    
    @Test
    public void testRegistrationWithUsernameWhichExists() throws Exception {
        User user = createUser();
        user.setEmail("sample1@mail.mail");
        RequestBuilder request = MockMvcRequestBuilders.post("/account/register").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(user));
        mvc.perform(request).andExpect(MockMvcResultMatchers.status().isConflict());
    }
    
    @Test
    public void testRegistrationWithEmailWhichExists() throws Exception {
        User user = createUser(); 
        user.setLogin("Login1");
        RequestBuilder request = MockMvcRequestBuilders.post("/account/register").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(user));
        mvc.perform(request).andExpect(MockMvcResultMatchers.status().isConflict());
    }
    
    @Test
    public void testRegistrationWithNullValues() throws Exception {
        User user = new User(); 
        RequestBuilder request = MockMvcRequestBuilders.post("/account/register").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(user));
        String[] expectedResultKeys = new String[] {"password", "gender", "surname", "name", "login", "birthDate", "email"};
        String resultJson = mvc.perform(request).andExpect(MockMvcResultMatchers.status().isBadRequest()).andReturn().getResponse().getContentAsString();
        Object[] resultKeys = mapper.readValue(resultJson, Map.class).keySet().toArray();
        Arrays.sort(expectedResultKeys);
        Arrays.sort(resultKeys);
        assertArrayEquals(expectedResultKeys, resultKeys);
    }
    
    @Test
    public void testRegistrationWithInvalidValuesLength() throws Exception {
        User user = new User(); 
        user.setBirthDate(new Date());
        user.setGender(Gender.FEMALE);
        user.setLogin("LoginLoginLoginLoginLoginLoginLogin");
        user.setPassword("PasswordPasswordPasswordPasswordPasswordPasswordPasswordPassword");
        user.setEmail("sample@mail.mailmailmailmailmailmailmailmailmailmailmailmailmailmailmailmailmailmailmailmailmailmailmailmailmailmailmailmailmailmailmailmailma"
                + "ilmailmailmailmailmailmailmailmailmailmailmailmailmailmailmailmailmailmailmailmailmailmailmailmailmailmailmailmail");
        user.setName("NameNameNameNameNameNameNameName");
        user.setSurname("SurnameSurnameSurnameSurnameSurname");
        RequestBuilder request = MockMvcRequestBuilders.post("/account/register").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(user));
        String[] expectedResultKeys = new String[] {"login", "password", "email", "name", "surname"};
        String resultJson = mvc.perform(request).andExpect(MockMvcResultMatchers.status().isBadRequest()).andReturn().getResponse().getContentAsString();
        Object[] resultKeys = mapper.readValue(resultJson, Map.class).keySet().toArray();
        Arrays.sort(expectedResultKeys);
        Arrays.sort(resultKeys);
        assertArrayEquals(expectedResultKeys, resultKeys);
    }
    
    @Test
    public void testRegistrationWithInvalidEmailFormat() throws Exception {
        User user = createUser(); 
        user.setEmail("mail@.");
        user.setLogin("Login1");
        RequestBuilder request = MockMvcRequestBuilders.post("/account/register").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(user));
        String[] expectedResultKeys = new String[] {"email"};
        String resultJson = mvc.perform(request).andExpect(MockMvcResultMatchers.status().isBadRequest()).andReturn().getResponse().getContentAsString();
        Object[] resultKeys = mapper.readValue(resultJson, Map.class).keySet().toArray();
        Arrays.sort(expectedResultKeys);
        Arrays.sort(resultKeys);
        assertArrayEquals(expectedResultKeys, resultKeys);
    }
    
    private User createUser() {
        User user = new User();
        user.setBirthDate(new Date());
        user.setEmail("sample@mail.mail");
        user.setGender(Gender.FEMALE);
        user.setLogin("Login");
        user.setName("Name");
        user.setPassword("Password");
        user.setSurname("Surname");
        return user;
    }
}
