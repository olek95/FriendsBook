package friendsbook.web;

public class ChatContactResource {
    private long id; 
    private String name; 
    private String surname; 
    private String login;
    
    public ChatContactResource() {}
    
    public long getId() { return id; }
    
    public void setId(long id) { this.id = id; }
    
    public String getName() { return name; }
    
    public void setName(String name) { this.name = name; }
    
    public String getSurname() { return surname; }
    
    public void setSurname(String surname) { this.surname = surname; }
    
    public void setLogin(String login) { this.login = login; }
    
    public String getLogin() { return login; }
}
