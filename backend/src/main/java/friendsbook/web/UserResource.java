package friendsbook.web;

import friendsbook.domain.user.Gender;
import java.util.Date;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.Email;

public class UserResource {
    private long id;
    @Size(min=1, max=30)
    @NotNull
    @Pattern(regexp = "[^@]+")
    private String login;
    @NotNull
    @Size(min=1, max=60)
    private String password; 
    @Size(min=1, max=254)
    @Email
    @NotNull
    private String email;
    @Size(min=1, max=30)
    @NotNull
    private String name; 
    @Size(min=1, max=30)
    @NotNull
    private String surname; 
    @NotNull
    private Date birthDate;
    @NotNull
    private Gender gender;
    
    public UserResource() {}
    
    public long getId() { return id; }
    
    public void setId(long id) { this.id = id; }
    
    public String getLogin() { return login; }
    
    public void setLogin(String login) { this.login = login; }
    
    public String getPassword() { return password; }
    
    public void setPassword(String password) { this.password = password; }
    
    public String getEmail() { return email; }
    
    public void setEmail(String email) { this.email = email; }
    
    public String getName() { return name; }
    
    public void setName(String name) { this.name = name; }
    
    public String getSurname() { return surname; }
    
    public void setSurname(String surname) { this.surname = surname; }
    
    public Date getBirthDate() { return birthDate; }
    
    public void setBirthDate(Date birthDate) { this.birthDate = birthDate; }
    
    public Gender getGender() { return gender; }
    
    public void setGender(Gender gender) { this.gender = gender; }
}
