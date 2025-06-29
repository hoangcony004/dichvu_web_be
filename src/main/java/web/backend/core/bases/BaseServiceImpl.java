package web.backend.core.bases;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public abstract class BaseServiceImpl<T extends BaseEntity, ID> implements BaseService<T, ID> {

    protected final JpaRepository<T, ID> repository;
    protected final JpaSpecificationExecutor<T> specificationExecutor;
    protected final Class<T> entityClass;

    public BaseServiceImpl(JpaRepository<T, ID> repository,
            JpaSpecificationExecutor<T> specificationExecutor, Class<T> entityClass) {
        this.specificationExecutor = specificationExecutor;
        this.repository = repository;
        this.entityClass = entityClass;
    }

    @Override
    public T create(T entity) {
        try {
            return repository.save(entity);
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi thêm mới: " + e.getMessage(), e);
        }
    }

    @Override
    public T update(ID id, T entity) {
        try {
            entity.setId((Long) id);
            return repository.save(entity);
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi cập nhật: " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(ID id) {
        try {
            repository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi xóa: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<T> findById(ID id) {
        try {
            return repository.findById(id);
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi tìm theo ID: " + e.getMessage(), e);
        }
    }

    @Override
    public Page<T> findAll(Pageable pageable, String keyword) {
        try {
            if (keyword != null && !keyword.trim().isEmpty()) {
                Specification<T> spec = new GenericSearchSpecification<>(keyword);
                // return repository.findAll(spec, pageable);
                return specificationExecutor.findAll(spec, pageable);
            } else {
                return repository.findAll(pageable);
            }
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi phân trang tìm kiếm: " + e.getMessage(), e);
        }
    }

    @Override
    public Map<String, List<Object>> getDataByColumn(String columnName) {
        List<T> allData = repository.findAll();
        Map<String, List<Object>> result = new HashMap<>();

        try {
            Field field = getAllFields(entityClass).stream()
                    .filter(f -> f.getName().equals(columnName))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy cột: " + columnName));

            field.setAccessible(true);

            List<Object> values = new ArrayList<>();
            for (T item : allData) {
                values.add(field.get(item));
            }

            result.put(columnName, values);
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi lấy dữ liệu: " + e.getMessage(), e);
        }

        return result;
    }

    public List<Field> getAllFields(Class<?> clazz) {
        List<Field> fields = new ArrayList<>();
        while (clazz != null && clazz != Object.class) {
            fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
            clazz = clazz.getSuperclass();
        }
        return fields;
    }

}