package ewm.event;

import ewm.event.model.Event;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.time.LocalDateTime;

public class EventRepositoryImpl implements EventRepositoryCustom {

    @Autowired
    private EntityManager entityManager;

    @Override
    public Event getPublicEvent(String text, Boolean paid, LocalDateTime rangeStart, LocalDateTime rangeEnd) {

        Query q;
        if (rangeEnd != null) {
            q = entityManager.createQuery("SELECT e FROM Event e WHERE e.category.id IN ?6" +
                    " AND e.annotation IN ?1 OR e.description IN ?1 AND e.paid = ?2 AND e.eventDate>?3" +
                    " AND e.eventDate < ?4", Event.class);
            q.setParameter(1, text);
            q.setParameter(2, paid);
            q.setParameter(3, rangeStart);
            q.setParameter(4, rangeEnd);
        } else {
            q = entityManager.createQuery("SELECT e FROM Event e WHERE e.category.id IN ?6" +
                            " AND e.annotation IN ?1 OR e.description IN ?1 AND e.paid = ?2 AND e.eventDate > ?3 ",
                    Event.class);
            q.setParameter(1, text);
            q.setParameter(2, paid);
            q.setParameter(3, rangeStart);
        }
        try {
            return (Event) q.getResultList().get(0);
        } catch (Exception e) {
            return null;
        }
    }

}
