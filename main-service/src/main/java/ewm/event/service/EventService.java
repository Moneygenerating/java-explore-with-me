package ewm.event.service;

import ewm.event.dto.EventFullDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EventService {
    List<EventFullDto> getAllPublic(Pageable pageable, String text, List<Long> categories, Boolean paid,
                              String rangeStart, String rangeEnd, Boolean onlyAvailable, String sort);

    EventFullDto getById(Long ids, String state);

    //Authorized User
    //Получение событий, добавленных текущим пользователем
    List<EventFullDto> getAllEventByIdPrivate(Long ids, Pageable pageable);

    //Изменение события
    EventFullDto pathEventByIdPrivate(Long userId, EventFullDto eventFullDto);

    //Добавить событие
    EventFullDto addEventPrivate(Long userId, EventFullDto eventFullDto);

    //Получение полной информации о событии добавленным ткущим пользователем
    EventFullDto getEventFullPrivate(Long userId, Long eventId);

    //отмена события добавленного текущим пользователем
    EventFullDto pathEventCansel(Long userId, Long eventId);

    //adm save and upd
    EventFullDto save(EventFullDto eventFullDto, Long id);

    //adm publish and reject
    EventFullDto updateEventAdm(Long id);

    void deleteUserById(Long userId);
}
