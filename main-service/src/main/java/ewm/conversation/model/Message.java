package ewm.conversation.model;

import ewm.user.model.User;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "messages")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String message;
    //creator of message
    @ManyToOne()
    private User user;
    private LocalDateTime createdOn;
    @ManyToOne(cascade = CascadeType.ALL)
    private Conversation conversation;
}
