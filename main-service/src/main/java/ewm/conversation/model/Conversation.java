package ewm.conversation.model;

import ewm.user.model.User;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "conversations")
public class Conversation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @ToString.Exclude
    private Set<Message> messages;
    @ManyToOne(cascade = CascadeType.ALL)
    private User creator;
    @ManyToOne(cascade = CascadeType.ALL)
    private User received;
    private LocalDateTime createdOn;
}
