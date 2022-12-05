package ewm.conversation.controllers;

import ewm.conversation.dto.ConversationDto;
import ewm.conversation.service.ConversationService;
import ewm.helper.Create;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(path = "/users/{userId}/conversations/{receivedId}")
public class UserConversationController {
    private ConversationService conversationService;

    @Autowired
    public UserConversationController(ConversationService conversationService) {
        this.conversationService = conversationService;
    }

    @PostMapping
    public ConversationDto postConversationAuth(@PathVariable Long userId, @PathVariable Long receivedId) {

        log.info("Запрос создание чата postConversationAuth /users/{userId}/conversations/{receivedId}");

        return conversationService.createConversation(userId, receivedId);
    }
}
