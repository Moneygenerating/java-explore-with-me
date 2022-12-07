package ewm.conversation;

import ewm.conversation.dto.ConversationDto;
import ewm.conversation.dto.MessageChatDto;
import ewm.conversation.dto.NewMessageDto;
import ewm.conversation.model.Conversation;
import ewm.conversation.model.Message;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

public class ConversationMapper {

    public static Message newMessageToDto(NewMessageDto newMessageDto) {
        return new Message(
                null,
                newMessageDto.getMessage(),
                //maybe set from body User
                null,
                LocalDateTime.now(),
                null
        );
    }

    public static MessageChatDto toMessageDto(Message message) {
        return new MessageChatDto(
                message.getId(),
                message.getMessage(),
                message.getUser().getName(),
                message.getCreatedOn()
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
                conversation.getMessages() != null ? conversation.getMessages().stream().map(ConversationMapper::toMessageShortDto)
                        .collect(Collectors.toList()) : null,
                conversation.getCreatedOn()
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
