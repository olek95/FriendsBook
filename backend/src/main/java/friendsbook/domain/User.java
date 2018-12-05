package friendsbook.domain;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class User implements Serializable {
    @Id
    private long id;
    private String login;
    private String password; 
    private String email;
    
    public User() {}
    
    public long getId() { return id; }
    
    public void setId(long id) { this.id = id; }
    
    public String getLogin() { return login; }
    
    public void setLogin(String login) { this.login = login; }
    
    public String getPassword() { return password; }
    
    public void setPassword(String password) { this.password = password; }
    
    public String getEmail() { return email; }
    
    public void setEmail(String email) { this.email = email; }
}
