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

    @DeleteMapping("/{convId}")
    public void deleteConversationByIdAdm(@PathVariable Long convId) {

        log.info("Запрос удаление чата deleteConversationByIdAdm  /admin/conversations/{convId}");
        conversationService.deleteById(convId);
    }

    @GetMapping("/users/{userId}")
    public List<ConversationDto> getAllConversationsByUserAuth(@RequestParam(required = false, defaultValue = "0") Integer from,
                                                               @RequestParam(required = false, defaultValue = "10") Integer size,
                                                               @PathVariable Long userId) {
        log.info("Запрос получения всех чатов пользователя getAllConversationsByUserAuth /admin/conversations/users/{userId}");
        return conversationService.findByCreatorId((PageRequest.of(from / size, size,
                Sort.by(Sort.Direction.ASC, "id"))), userId);
    }
}
