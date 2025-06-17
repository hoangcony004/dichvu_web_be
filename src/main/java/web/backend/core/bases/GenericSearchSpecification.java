package  web.backend.core.bases;

import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class GenericSearchSpecification<T> implements Specification<T> {
    private final String keyword;

    public GenericSearchSpecification(String keyword) {
        this.keyword = keyword == null ? "" : keyword.toLowerCase();
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        if (keyword.isEmpty()) {
            return cb.conjunction();
        }

        List<Predicate> predicates = new ArrayList<>();

        for (Field field : root.getJavaType().getDeclaredFields()) {
            if (field.getType().equals(String.class)) {
                try {
                    predicates.add(cb.like(cb.lower(root.get(field.getName())), "%" + keyword + "%"));
                } catch (IllegalArgumentException e) {
                    // Bỏ qua các trường không được mapping hoặc transient
                }
            }
        }

        return cb.or(predicates.toArray(new Predicate[0]));
    }
}
