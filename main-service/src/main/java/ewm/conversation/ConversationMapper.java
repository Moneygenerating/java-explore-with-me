package ewm.conversation;

import ewm.conversation.dto.ConversationDto;
import ewm.conversation.dto.MessageDto;
import ewm.conversation.model.Conversation;
import ewm.conversation.model.Message;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

public class ConversationMapper {
    public static Message newMessageToDto(MessageDto messageDto) {
        return new Message(
                null,
                messageDto.getMessage(),
                //maybe set from body User
                null,
                LocalDateTime.now()
        );
    }

    public static ConversationDto conversationToDto(Conversation conversation) {
        return new ConversationDto(
                conversation.getId(),
                new ConversationDto.UserShortDto(
                        conversation.getCreator().getId(),
                        conversation.getCreator().getName()
                ),
                new ConversationDto.UserShortDto(
                        conversation.getReceived().getId(),
                        conversation.getReceived().getName()
                ),
                conversation.getMessages()!= null ? conversation.getMessages().stream().map(ConversationMapper::toMessageShortDto)
                        .collect(Collectors.toSet()) : null,
                conversation.getCreatedOn()
        );
    }

    public static Conversation newDtoConversation() {
        return new Conversation(
                null,
                null,
                null,
                null,
                LocalDateTime.now()
        );
    }


    private static ConversationDto.MessageShortDto toMessageShortDto(Message message) {
        return new ConversationDto.MessageShortDto(
                message.getId(),
                message.getMessage(),
                message.getUser().getName(),
                message.getCreatedOn()
        );
    }
}
