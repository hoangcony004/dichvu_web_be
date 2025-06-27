package  web.backend.core.bases;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface BaseService<T, ID> {
    T create(T entity);
    T update(ID id, T entity);
    void delete(ID id);
    Optional<T> findById(ID id);
    Page<T> findAll(Pageable pageable, String keyword);

    Map<String, List<Object>> getDataByColumn(String columnName);
}