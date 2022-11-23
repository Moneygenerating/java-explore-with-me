package ewm.request.model;

import ewm.event.model.Event;
import ewm.event.model.StateLifecycle;
import ewm.user.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "participation_request")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParticipationRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(cascade = CascadeType.ALL)
    private Event event;
    @Column(name = "created", nullable = false)
    private LocalDateTime created;
    @ManyToOne(cascade = CascadeType.ALL)
    private User requester;
    @Column(name = "status")
    private Status status;
}
