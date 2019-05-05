package friendsbook.domain.conversation;

import friendsbook.domain.user.User;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Message implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false, length = 255)
    private String content;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "recipient_id", nullable = false)
    private User recipient;
    
    public Message() {}
    
    public long getId() { return id; }
    
    public void setId(long id) { this.id = id; }
    
    public String getContent() { return content; }
    
    public void setContent(String content) { this.content = content; }
    
    public User getSender() { return sender; }
    
    public void setSender(User sender) { this.sender = sender; }
    
    public User getRecipient() { return recipient; }
    
    public void setRecipient(User recipient) { this.recipient = recipient; }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + (int) (this.id ^ (this.id >>> 32));
        hash = 29 * hash + Objects.hashCode(this.content);
        hash = 29 * hash + Objects.hashCode(this.sender);
        hash = 29 * hash + Objects.hashCode(this.recipient);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Message)) {
            return false;
        }
        final Message other = (Message) obj;
        
        return this.id == other.id && Objects.equals(this.content, other.content)
                && Objects.equals(this.sender, other.sender) && Objects.equals(this.recipient, other.recipient);
    }
}
