package ewm.compilation.service;

import ewm.compilation.CompilationMapper;
import ewm.compilation.CompilationRepository;
import ewm.compilation.dto.CompilationDto;
import ewm.compilation.dto.NewCompilationDto;
import ewm.compilation.model.Compilation;
import ewm.errors.NotFoundException;
import ewm.event.EventRepository;
import ewm.event.model.Event;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class CompilationServiceImpl implements CompilationService {
    @Autowired
    private CompilationRepository compilationRepository;

    @Autowired
    private EventRepository eventRepository;

    @Override
    public List<CompilationDto> getAll(Pageable pageable, Boolean pinned) {
        return compilationRepository.findAll(
                        (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("pinned"), pinned),
                        pageable)
                .stream()
                .map(CompilationMapper::compilationToDto)
                .collect(Collectors.toList());
    }

    @Override
    public CompilationDto getById(Long compId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Подборка с таким id не найдена"));
        return CompilationMapper.compilationToDto(compilation);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public CompilationDto createComp(NewCompilationDto newCompilationDto) {
        Compilation compilation = CompilationMapper.newDtoToCompilation(newCompilationDto);
        //set events in chronological
        compilation.setEvents(Set.copyOf(eventRepository.findAllById(newCompilationDto.getEvents())));
        compilationRepository.save(compilation);
        return CompilationMapper.compilationToDto(compilation);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public void deleteCompById(Long compId) {
        compilationRepository.deleteById(compId);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public void deleteEventInCompById(Long compId, Long eventId) {
        Compilation compilation = compilationRepository.findById(compId).orElseThrow(
                () -> new NotFoundException("Подборка с таким id не найдена"));
        Set<Event> events = compilation.getEvents();
        events.remove(eventRepository.getReferenceById(eventId));
        compilation.setEvents(events);
        compilationRepository.save(compilation);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public void addEventInCompById(Long compId, Long eventId) {
        Compilation compilation = compilationRepository.findById(compId).orElseThrow(
                () -> new NotFoundException("Подборка с таким id не найдена"));
        Set<Event> events = compilation.getEvents();
        events.add(eventRepository.getReferenceById(eventId));
        compilation.setEvents(events);
        compilationRepository.save(compilation);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public void deleteCompPin(Long compId) {
        Compilation compilation = compilationRepository.findById(compId).orElseThrow(
                () -> new NotFoundException("Подборка с таким id не найдена"));
        compilation.setPinned(false);
        compilationRepository.save(compilation);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public void addCompPin(Long compId) {
        Compilation compilation = compilationRepository.findById(compId).orElseThrow(
                () -> new NotFoundException("Подборка с таким id не найдена"));
        compilation.setPinned(true);
        compilationRepository.save(compilation);
    }
}
