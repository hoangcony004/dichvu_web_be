package web.backend.modules.repository.admin.system.sys_user;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import web.backend.core.entitys.systems.SysUser;

public interface SysUserRepository extends JpaRepository<SysUser, Long>, JpaSpecificationExecutor<SysUser> {

    SysUser findByUsername(String username);

    @Query("SELECT r.name FROM SysUserRole ur " +
            "JOIN ur.user u " +
            "JOIN ur.role r " +
            "WHERE u.username = :username")
    List<String> findRolesByUsername(@Param("username") String username);

}
