package friendsbook.domain;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
public class User implements Serializable {
    @Id
    private long id;
    @Column(nullable = false)
    private String login;
    @Column(nullable = false)
    private String password; 
    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    private String name; 
    @Column(nullable = false)
    private String surname; 
    @Column(nullable = false)
    private Date birthDate;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    
    public User() {}
    
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
