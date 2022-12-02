package ewm.helper;

import org.springframework.data.domain.PageRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;


public abstract class AbstractController<T> {

    protected AbstractService<T> service;

    @PostMapping
    public T create(@Valid @RequestBody T item) {
        return service.create(item);
    }

    @PatchMapping
    public T update(@Validated({Update.class}) @RequestBody T item,
                    @PathVariable Long id) {
        return service.update(id, item);
    }

    @GetMapping()
    public List<T> get(@RequestParam(value = "from", required = false, defaultValue = "0")
                       @PositiveOrZero int from,
                       @RequestParam(value = "size", required = false, defaultValue = "10") int size,
                       @RequestParam(value = "ids") List<Long> ids) {
        return service.get(PageRequest.of(from / size, size), ids);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        service.delete(id);
    }
}
