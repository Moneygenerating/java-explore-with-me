package ewm.conversation.controllers.admin;

import ewm.conversation.dto.ConversationDto;
import ewm.conversation.dto.NewMessageDto;
import ewm.conversation.service.ConversationService;
import ewm.helper.Create;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(path = "/admin/conversations")
public class AdminConversationController {
    private ConversationService conversationService;

    @Autowired
    public AdminConversationController(ConversationService conversationService) {
        this.conversationService = conversationService;
    }

    @GetMapping("/{convId}")
    public ConversationDto getConversationByIdAdm(@PathVariable Long convId) {

        log.info("Запрос получения чата getConversationByIdAdm /admin/conversations/{convId}");
        return conversationService.getById(convId);
    }


    @PostMapping("/{receivedId}")
    public ConversationDto postConversationAuth(@PathVariable Long userId, @PathVariable Long receivedId) {

        log.info("Запрос создание чата postConversationAuth /users/{userId}/conversations/{receivedId}");

        return conversationService.createConversation(userId, receivedId);
    }

    @PostMapping("/{receivedId}/message")
    public ConversationDto postMessageAuth(@PathVariable Long userId, @PathVariable Long receivedId,
                                           @Validated(Create.class) @RequestBody NewMessageDto newMessageDto) {

        log.info("Запрос отправку сообщения в чате postMessageAuth /users/{userId}/conversations/{receivedId}/message");

        return conversationService.addMessageInConversationById(userId, receivedId, newMessageDto);
    }

    @GetMapping
    public List<ConversationDto> getAllConversationsByUserAuth(@RequestParam(required = false, defaultValue = "0") Integer from,
                                                               @RequestParam(required = false, defaultValue = "10") Integer size,
                                                               @PathVariable Long userId) {
        log.info("Запрос получения всех чатов пользователя getAllConversationsByUserAuth /users/{userId}/conversations");
        return conversationService.getOwnConversations((PageRequest.of(from / size, size,
                Sort.by(Sort.Direction.ASC, "id"))), userId);
    }

    @DeleteMapping("/{receivedId}")
    public void deleteConversationAuth(@PathVariable Long userId, @PathVariable Long receivedId) {

        log.info("Запрос удаление чата deleteConversationAuth /users/{userId}/conversations/{receivedId}");
        conversationService.deleteConversationByOwnId(userId, receivedId);
    }
}
