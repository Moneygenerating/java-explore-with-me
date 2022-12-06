package ewm.conversation.service;

import ewm.conversation.ConversationMapper;
import ewm.conversation.ConversationRepository;
import ewm.conversation.MessageRepository;
import ewm.conversation.dto.ConversationDto;
import ewm.conversation.dto.NewMessageDto;
import ewm.conversation.model.Conversation;
import ewm.conversation.model.Message;
import ewm.errors.NotFoundException;
import ewm.errors.ValidationException;
import ewm.user.UserRepository;
import ewm.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

        // Создаем диалог
        Conversation conversationSaved = conversationRepository.save(new Conversation(
                null,
                null,
                userRepository.getReferenceById(creatorId),
                userRepository.getReferenceById(receivedId),
                LocalDateTime.now()
        ));

        //Создаем приветственное сообщение
        Message message = new Message(
                null,
                "Добро пожаловать в чат для обсуждения предстоящих событий! Сообщение создано автоматически." +
                        " Удачного общения!",
                userRepository.getReferenceById(creatorId),
                LocalDateTime.now(),
                conversationSaved
        );
        //Сохраняем сообщения
        messageRepository.save(message);

        //update messages in conversation
        Set<Message> messages = new HashSet<>(
                Set.copyOf(messageRepository.findAllByConversationId(conversationSaved.getId()))
        );

        conversationSaved.setMessages(messages);
        conversationRepository.save(conversationSaved);

        return ConversationMapper.conversationToDto(conversationSaved);
    }

    @Override
    public ConversationDto addMessageInConversationById(Long creatorId, Long receivedId, NewMessageDto newMessageDto) {
        if (checkExistConversation(creatorId, receivedId)) {
            //get conversation
            Conversation conversationSaved = conversationRepository.getByCreatorIdAndReceivedId(creatorId, receivedId);

            //preparing message
            Message message = ConversationMapper.newMessageToDto(newMessageDto);
            message.setUser(userRepository.getReferenceById(creatorId));
            message.setConversation(conversationSaved);
            //save message in repo
            messageRepository.save(message);

            //get messages in conversation
            Set<Message> messages = new HashSet<>(
                    Set.copyOf(messageRepository.findAllByConversationId(conversationSaved.getId()))
            );

            //add message
            messages.add(message);
            //save
            conversationSaved.setMessages(messages);
            return ConversationMapper.conversationToDto(conversationRepository.save(conversationSaved));
        }

        //recursion create conversation
        createConversation(creatorId, receivedId);
        return addMessageInConversationById(creatorId, receivedId, newMessageDto);
    }


    @Override
    public List<ConversationDto> getOwnConversations(Pageable pageable, Long userId) {
        User user = userRepository.getReferenceById(userId);
        return conversationRepository.findAll(
                        (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("creator"), user),
                        pageable)
                .stream()
                .map(ConversationMapper::conversationToDto)
                .collect(Collectors.toList());
    }

    @Override
    public ConversationDto getConversationByReceivedUser(Long creatorId, Long receivedId) {
        Conversation conversation = conversationRepository.getByCreatorIdAndReceivedId(creatorId, receivedId);
        if (conversation == null) {
            throw new NotFoundException("Диалог не найден");
        }
        return ConversationMapper.conversationToDto(conversation);
    }

    @Override
    public void deleteConversationByOwnId(Long creatorId, Long receivedId) {
        Conversation conversation = conversationRepository.getByCreatorIdAndReceivedId(creatorId, receivedId);
        conversationRepository.deleteById(conversation.getId());
    }

    private boolean checkExistConversation(Long creatorId, Long receivedId) {
        Conversation conversation = conversationRepository.getByCreatorIdAndReceivedId(creatorId, receivedId);
        Conversation conversationAnother = conversationRepository.getByCreatorIdAndReceivedId(receivedId, creatorId);

        if (conversation != null) {
            return true;
        }

        return conversationAnother != null;
    }
}
