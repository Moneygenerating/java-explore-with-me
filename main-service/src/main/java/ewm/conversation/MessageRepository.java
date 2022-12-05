package ewm.conversation;

import ewm.conversation.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long>,
        JpaSpecificationExecutor<Message>  {
    List<Message> findAllByUserIdAndUserOutId(Long creatorId, Long userOutId);
}
