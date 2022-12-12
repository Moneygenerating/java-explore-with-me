package ewm.request;

import ewm.event.model.Event;
import ewm.request.model.ParticipationRequest;
import ewm.user.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface RequestRepository extends JpaRepository<ParticipationRequest, Long>, ReqRepoCustom,
        JpaSpecificationExecutor<ParticipationRequest> {

    ParticipationRequest findByRequesterAndEvent(User user, Event event);

    ParticipationRequest findByIdAndRequesterId(Long id, Long requesterId);

    List<ParticipationRequest> findAllByRequesterId(Long userId);

    @Override
    @EntityGraph(attributePaths = {"event", "requester"})
    Page<ParticipationRequest> findAll(Specification<ParticipationRequest> spec, Pageable pageable);
}
