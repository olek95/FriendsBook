package config;

import friendsbook.config.WebConfiguration;
import friendsbook.domain.Gender;
import friendsbook.service.UserService;
import friendsbook.web.UserResource;
import java.util.Base64;
import java.util.Date;
import javax.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {WebConfiguration.class})
@WebAppConfiguration
@Transactional
public class SecurityConfigurationTest {
    @Autowired
    private UserService userService;
    @Autowired
    private WebApplicationContext webApplicationContext;

    private boolean dbFilled = false;
    private MockMvc mvc;
    private static final String AUTHORIZATION_PREFIX = "Basic";
    private UserResource user = new UserResource();

    @BeforeEach
    public void setup() {
        if (!dbFilled) {
            user.setBirthDate(new Date());
            user.setEmail("sample@mail.mail");
            user.setGender(Gender.FEMALE);
            user.setLogin("Login");
            user.setName("Name");
            user.setPassword("Password");
            user.setSurname("Surname");
            userService.save(user);
            dbFilled = true;
        }
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).apply(SecurityMockMvcConfigurers.springSecurity()).build();
    }
    
    @Test
    public void testUserLoginByUsername() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.get("/account/login")
                .header(HttpHeaders.AUTHORIZATION, getAuthorizationHeader(user.getLogin(), user.getPassword()));
        mvc.perform(request).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.header().exists(HttpHeaders.AUTHORIZATION));
    }
    
    @Test
    public void testUserLoginByEmail() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.get("/account/login")
                .header(HttpHeaders.AUTHORIZATION, getAuthorizationHeader(user.getEmail(), user.getPassword()));
        mvc.perform(request).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.header().exists(HttpHeaders.AUTHORIZATION));
    }
    
    @Test
    public void testUserLoginWithoutName() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.get("/account/login")
                .header(HttpHeaders.AUTHORIZATION, getAuthorizationHeader("", user.getPassword()));
        mvc.perform(request).andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }
    
    @Test
    public void testUserLoginWithoutPassword() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.get("/account/login")
                .header(HttpHeaders.AUTHORIZATION, getAuthorizationHeader(user.getLogin(), ""));
        mvc.perform(request).andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }
    
    @Test
    public void testUserLoginWithBadPassword() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.get("/account/login")
                .header(HttpHeaders.AUTHORIZATION, getAuthorizationHeader(user.getLogin(), "otherPassword"));
        mvc.perform(request).andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }
    
    @Test
    public void testUserLoginWithoutAuthorizationPrefixAndDataShorterThanPrefixLength() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.get("/account/login")
                .header(HttpHeaders.AUTHORIZATION, new String(Base64.getEncoder().encode("Lo".getBytes())));
        mvc.perform(request).andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }
    
    @Test
    public void testCors() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.options("/account/login")
                .header(HttpHeaders.AUTHORIZATION, getAuthorizationHeader(user.getLogin(), user.getPassword()))
                .header("Access-Control-Request-Method", "GET")
                .header("Origin", "http://localhost:4200");
        mvc.perform(request).andExpect(MockMvcResultMatchers.status().isOk());
    }
    
    private String getAuthorizationHeader(String name, String password) {
        return AUTHORIZATION_PREFIX + " " + new String(Base64.getEncoder().encode((name + ":" + password).getBytes()));
    }
}
