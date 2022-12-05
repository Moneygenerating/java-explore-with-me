package ewm.conversation;

import ewm.conversation.model.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface ConversationRepository extends JpaRepository<Conversation, Long>,
        JpaSpecificationExecutor<Conversation> {

    List<Conversation> findAllByCreatorId(Long creatorId);

    Conversation getByCreatorIdAndReceivedId(Long creatorId, Long receivedId);
}
