package friendsbook.domain.user;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false, unique = true, length = 30)
    private String login;
    @Column(nullable = false, length = 60)
    private String password; 
    @Column(nullable = false, length = 254)
    private String email;
    @Column(nullable = false, length = 30)
    private String name; 
    @Column(nullable = false, length = 30)
    private String surname; 
    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Date birthDate;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "ENUM('FEMALE', 'MALE')")
    private Gender gender;
    
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "friendship", joinColumns = @JoinColumn(name = "friend1_id"), inverseJoinColumns = @JoinColumn(name = "friend2_id"))
    private List<User> friends;
    
    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "friends")
    private List<User> friendsOf;
    
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
    
    public List<User> getFriends() { return friends; }
    
    public void setFriends(List<User> friends) { this.friends = friends; }
    
    public List<User> getFriendsOf() { return friendsOf; }
    
    public void setFriendsOf(List<User> friendsOf) { this.friendsOf = friendsOf; }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + Objects.hashCode(login);
        hash = 29 * hash + Objects.hashCode(email);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof User)) {
            return false;
        }
        User user = (User)obj;
        return Objects.equals(login.toLowerCase(), user.login.toLowerCase()) 
                && Objects.equals(formatEmailDomainPartToCaseInsensitive(email), formatEmailDomainPartToCaseInsensitive(user.email));
    }
    
    private String formatEmailDomainPartToCaseInsensitive(String email) {
        String[] parts = email.split("@");
        return parts[0] + "@" + parts[1].toLowerCase();
    }
}
