package ewm.helper;

import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AbstractService<T> {
    T create(T item);

    T update(Long id, T item);

    //forAll(pageable and iterate by id)
    List<T> get(Pageable pageable, List<Long> ids);

    //byId - polymorphic
    List<T> get(Long ids);

    void delete(Long id);

}
