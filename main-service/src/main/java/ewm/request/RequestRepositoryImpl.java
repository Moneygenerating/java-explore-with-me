package ewm.request;

import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.Query;

public class RequestRepositoryImpl implements ReqRepoCustom {

    @Autowired
    private EntityManager entityManager;

    @Override
    public Integer getCountOfConfirmedRequests(Long idsEvents){
     Query q = entityManager
             .createQuery("SELECT count(p.event.id) FROM ParticipationRequest p WHERE p.event.id = ?1", Integer.class);
        q.setParameter(1, idsEvents);
        try {
            return (Integer) q.getResultList().get(0);
        } catch (Exception e) {
            return null;
        }
    }
}
