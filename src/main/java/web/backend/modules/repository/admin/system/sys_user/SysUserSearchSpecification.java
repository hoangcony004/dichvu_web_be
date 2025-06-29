package web.backend.modules.repository.admin.system.sys_user;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import web.backend.core.entitys.systems.SysRole;
import web.backend.core.entitys.systems.SysUser;
import web.backend.core.entitys.systems.SysUserRole;

public class SysUserSearchSpecification implements Specification<SysUser> {

    private final String keyword;
    private final String roleName;

    public SysUserSearchSpecification(String keyword, String roleName) {
        this.keyword = (keyword == null) ? "" : keyword.toLowerCase();
        this.roleName = (roleName == null) ? "" : roleName.toLowerCase();
    }

    @Override
    public Predicate toPredicate(Root<SysUser> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        List<Predicate> keywordPredicates = new ArrayList<>();

        if (!keyword.isEmpty()) {
            String likeKeyword = "%" + keyword + "%";
            for (Field field : SysUser.class.getDeclaredFields()) {
                if (field.getType().equals(String.class)) {
                    try {
                        keywordPredicates.add(cb.like(cb.lower(root.get(field.getName())), likeKeyword));
                    } catch (IllegalArgumentException ignored) {
                    }
                }
            }
        }

        Predicate keywordPredicate = null;
        if (!keywordPredicates.isEmpty()) {
            keywordPredicate = cb.or(keywordPredicates.toArray(new Predicate[0]));
        }

        Predicate rolePredicate = null;
        if (!roleName.isEmpty()) {
            Join<SysUser, SysUserRole> userRoleJoin = root.join("sysUserRoles", JoinType.LEFT);
            Join<SysUserRole, SysRole> roleJoin = userRoleJoin.join("role", JoinType.LEFT);
            rolePredicate = cb.equal(cb.lower(roleJoin.get("name")), roleName);
        }

        if (keywordPredicate != null && rolePredicate != null) {
            return cb.and(keywordPredicate, rolePredicate);
        } else if (keywordPredicate != null) {
            return keywordPredicate;
        } else if (rolePredicate != null) {
            return rolePredicate;
        } else {
            return cb.conjunction();
        }
    }

}
