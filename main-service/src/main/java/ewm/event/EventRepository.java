package ewm.event;

import ewm.event.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long>, EventRepositoryCustom {
    @Modifying
    @Query("SELECT e FROM Event e where e.id=?1 AND e.state = 'PUBLISHED' ")
    Event findByIdAndState(Long eventId);

    List<Event> getAllByInitiatorId(Long initiatorId);

    Event getEventByIdAndInitiatorId(Long id, Long initiatorId);
}
