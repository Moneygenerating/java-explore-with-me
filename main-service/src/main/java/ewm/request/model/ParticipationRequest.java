package ewm.request.model;

import ewm.event.model.Event;
import ewm.user.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

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
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ParticipationRequest participationRequest = (ParticipationRequest) o;
        return id != null && Objects.equals(id, participationRequest.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}