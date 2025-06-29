package web.backend.modules.service.admin.system.sys_user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import web.backend.core.bases.BaseService;
import web.backend.core.entitys.systems.SysUser;

public interface SysUserService extends BaseService<SysUser, Long> {
    // Thêm method đặc thù ở đây nếu cần

    Page<SysUser> findAllCustom(Pageable pageable, String keyword, String role);

}
