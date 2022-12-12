package ewm.event;

import ewm.event.model.Event;
import ewm.event.model.StateLifecycle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long>, JpaSpecificationExecutor<Event> {

    @Query("SELECT e FROM Event e where e.id=?1 AND e.state = ?2 ")
    Event findByIdAndState(Long eventId, StateLifecycle stateLifecycle);

    Event getEventByIdAndInitiatorId(Long id, Long initiatorId);

    List<Event> findAllByInitiatorId(Long userId);

    @Override
    @EntityGraph(attributePaths = {"category", "initiator"})
    Page<Event> findAll(Specification<Event> spec, Pageable pageable);
}
