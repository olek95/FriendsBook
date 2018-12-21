package friendsbook.domain;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.Email;

@Entity
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false, unique = true)
    @Size(min=1, max=30)
    @NotNull
    private String login;
    @Column(nullable = false)
    @NotNull
    @Size(min=1, max=60)
    private String password; 
    @Column(nullable = false, unique = true)
    @Size(min=1, max=254)
    @Email
    @NotNull
    private String email;
    @Column(nullable = false)
    @Size(min=1, max=30)
    @NotNull
    private String name; 
    @Column(nullable = false)
    @Size(min=1, max=30)
    @NotNull
    private String surname; 
    @Column(nullable = false)
    @NotNull
    @Temporal(TemporalType.DATE)
    private Date birthDate;
    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(nullable = false, columnDefinition = "ENUM('FEMALE', 'MALE')")
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
