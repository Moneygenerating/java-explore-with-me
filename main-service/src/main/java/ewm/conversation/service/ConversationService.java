package ewm.conversation.service;

import ewm.conversation.dto.ConversationDto;
import ewm.conversation.dto.NewMessageDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ConversationService {

    //Получение чата по id Admin
    ConversationDto getById(Long convId);

    //Удаление чата Admin
    void deleteById(Long convId);

    //Поиск всех чатов любого пользователя Admin
    List<ConversationDto> findByCreatorId(Pageable pageable, Long userId);

    //создать чат принудительно (опция работает при условии, что нужно задать имя чату)
    //обычно чат создается автоматически, если пользователь напишет другому в личку
    ConversationDto createConversation(Long creatorId, Long receivedId);

    //Написать сообщение
    ConversationDto addMessageInConversationById(Long creatorId, Long receivedId, NewMessageDto newMessageDto);

    //Получить все свои чаты
    List<ConversationDto> getOwnConversations(Pageable pageable,Long userId);

    //Получить чат с конкретным пользователем
    ConversationDto getConversationByReceivedUser(Long creatorId, Long receivedId);

    //Удалить чат - пользователь может удалять только свой чат
    void deleteConversationByOwnId(Long creatorId, Long receivedId);

}
