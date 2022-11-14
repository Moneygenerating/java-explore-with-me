package ewm.helper;

import java.util.List;

public interface AbstractService<T> {
    T create(T item);

    T update(T item);

    T get(Long id);

    List<T> getAll();

    void delete(Long id);
}
