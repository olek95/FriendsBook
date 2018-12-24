package config;

import friendsbook.config.WebConfiguration;
import friendsbook.domain.Gender;
import friendsbook.domain.User;
import friendsbook.service.UserService;
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

    @BeforeEach
    public void setup() {
        if (!dbFilled) {
            User user = new User();
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
                .header(HttpHeaders.AUTHORIZATION, "Basic " + new String(Base64.getEncoder().encode("Login:Password".getBytes())));
        mvc.perform(request).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.header().exists(HttpHeaders.AUTHORIZATION));
    }
    
    @Test
    public void testUserLoginByEmail() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.get("/account/login")
                .header(HttpHeaders.AUTHORIZATION, "Basic " + new String(Base64.getEncoder().encode("sample@mail.mail:Password".getBytes())));
        mvc.perform(request).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.header().exists(HttpHeaders.AUTHORIZATION));
    }
    
    @Test
    public void testCors() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.options("/account/login")
                .header(HttpHeaders.AUTHORIZATION, "Basic " + new String(Base64.getEncoder().encode("Login:Password".getBytes())))
                .header("Access-Control-Request-Method", "GET")
                .header("Origin", "http://localhost:4200");
        mvc.perform(request).andExpect(MockMvcResultMatchers.status().isOk());
    }
}
