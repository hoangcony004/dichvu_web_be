package  web.backend.core.bases;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public abstract class BaseServiceImpl<T extends BaseEntity, ID> implements BaseService<T, ID> {

    protected final JpaRepository<T, ID> repository;
    protected final JpaSpecificationExecutor<T> specificationExecutor;

    public BaseServiceImpl(JpaRepository<T, ID> repository,
            JpaSpecificationExecutor<T> specificationExecutor) {
        this.specificationExecutor = specificationExecutor;
        this.repository = repository;
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
            Specification<T> spec = new GenericSearchSpecification<>(keyword);
            return specificationExecutor.findAll(spec, pageable);
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi phân trang tìm kiếm: " + e.getMessage(), e);
        }
    }

}