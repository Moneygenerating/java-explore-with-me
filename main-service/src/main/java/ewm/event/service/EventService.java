package ewm.event.service;

import ewm.event.dto.EventAdminDto;
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

    //adm getAll
    List<EventFullDto> getAllAdmin(Pageable pageable, List<Long> users, List<String> states, List<Long> categories,
                                    String rangeStart, String rangeEnd);

    //adm save and upd
    EventFullDto saveAdmin(EventAdminDto eventAdminDto, Long id);

    //adm publish and reject
    EventFullDto publishEventAdmin(Long id);

    EventFullDto rejectEventAdmin(Long id);

}
