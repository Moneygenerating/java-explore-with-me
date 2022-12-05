package ewm.conversation.service;

import ewm.conversation.ConversationMapper;
import ewm.conversation.ConversationRepository;
import ewm.conversation.MessageRepository;
import ewm.conversation.dto.ConversationDto;
import ewm.conversation.dto.MessageDto;
import ewm.conversation.model.Conversation;
import ewm.conversation.model.Message;
import ewm.errors.ValidationException;
import ewm.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

@Service
public class ConversationServiceImpl implements ConversationService {

    private final ConversationRepository conversationRepository;

    private final MessageRepository messageRepository;

    private final UserRepository userRepository;

    @Autowired
    public ConversationServiceImpl(ConversationRepository conversationRepository,
                                   MessageRepository messageRepository, UserRepository userRepository) {
        this.conversationRepository = conversationRepository;
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
    }

    @Override
    public ConversationDto getById(Long convId) {
        return null;
    }

    @Override
    public void deleteById(Long convId) {

    }

    @Override
    public List<ConversationDto> findByCreatorId(Long userId) {
        return null;
    }

    //создать чат принудительно (опция работает при условии, что нужно задать имя чату)
    //обычно чат создается автоматически, если пользователь напишет другому в личку
    @Override
    public ConversationDto createConversation(Long creatorId, Long receivedId) {
        //Проверка на существование диалога
        if (checkExistConversation(creatorId, receivedId)) {
            throw new ValidationException("Нельзя создать новый диалог с пользователем, так как он уже существует");
        }

        //Создаем диалог
        Conversation conversation = ConversationMapper.newDtoConversation();
        conversation.setCreator(userRepository.getReferenceById(creatorId));
        conversation.setReceived(userRepository.getReferenceById(receivedId));

        //Создаем приветственное сообщение
        Message message = new Message(
                null,
                "Добро пожаловать в чат для обсуждения предстоящих событий! Сообщение создано автоматически." +
                        " Удачного общения!",
                userRepository.getReferenceById(creatorId),
                LocalDateTime.now(),
                conversation.getReceived()
        );
        //Сохраняем
        messageRepository.save(message);

        Set<Message> messages = Set.copyOf(messageRepository.findAllByUserIdAndUserOutId(creatorId, receivedId));

        conversation.setMessages(messages);
        conversationRepository.save(conversation);

        return ConversationMapper.conversationToDto(conversation);
    }

    @Override
    public void deleteByOwnId(Long UserId, Long convId) {

    }

    @Override
    public void addMessageInConversationById(Long convId, Long creatorId, Long receivedId, MessageDto messageDto) {

    }

    @Override
    public List<ConversationDto> getOwnConversations(Long userId) {
        return null;
    }

    @Override
    public ConversationDto getConversationByReceivedUser(Long creatorId, Long receivedId) {
        return null;
    }

    private boolean checkExistConversation(Long creatorId, Long receivedId) {
        Conversation conversation = conversationRepository.getByCreatorIdAndReceivedId(creatorId, receivedId);
        Conversation conversationAnother = conversationRepository.getByCreatorIdAndReceivedId(receivedId, creatorId);

        if (conversation != null) {
            return true;
        }

        if (conversationAnother != null) {
            return true;
        }
        return false;

    }
}
