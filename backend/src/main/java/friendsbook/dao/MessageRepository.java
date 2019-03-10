package friendsbook.dao;

import friendsbook.domain.conversation.Message;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    @Query("SELECT m FROM Message m WHERE sender.id IN(?1, ?2) AND recipient.id IN (?1, ?2)")
    List<Message> findConversation(long userId, long correspondentId);
}
