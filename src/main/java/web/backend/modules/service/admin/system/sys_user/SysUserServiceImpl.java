package web.backend.modules.service.admin.system.sys_user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import web.backend.core.bases.BaseServiceImpl;
import web.backend.core.entitys.systems.SysUser;
import web.backend.modules.repository.admin.system.sys_user.SysUserRepository;
import web.backend.modules.repository.admin.system.sys_user.SysUserSearchSpecification;

@Service
public class SysUserServiceImpl extends BaseServiceImpl<SysUser, Long> implements SysUserService {

    private final SysUserRepository sysUserRepository;

    public SysUserServiceImpl(SysUserRepository sysUserRepository) {
        super(sysUserRepository, sysUserRepository, SysUser.class);
        this.sysUserRepository = sysUserRepository;
    }

    @Override
    public Page<SysUser> findAllCustom(Pageable pageable, String keyword, String role) {
        Specification<SysUser> spec = new SysUserSearchSpecification(keyword, role);
        return sysUserRepository.findAll(spec, pageable);
    }

}
