package friendsbook.web;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class MessageResource {
    @Size(min = 1, max = 255)
    @NotNull
    private String content; 
    private long senderId; 
    private long recipientId; 
    
    public MessageResource() {}
    
    public String getContent() { return content; }
    
    public void setContent(String content) { this.content = content; }
    
    public long getSenderId() { return senderId; }
    
    public void setSenderId(long senderId) { this.senderId = senderId; }
    
    public long getRecipientId() { return recipientId; }
    
    public void setRecipientId(long recipientId) { this.recipientId = recipientId; }
}
