package web.backend.modules.repository.admin.system;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import web.backend.core.entitys.systems.SysToken;

@Repository
public interface SysTokenRepository extends JpaRepository<SysToken, Long> {
    boolean existsByTokenAndIsRevokedTrue(String token);
} 