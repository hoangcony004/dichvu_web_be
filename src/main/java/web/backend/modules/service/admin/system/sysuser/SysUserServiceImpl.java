package web.backend.modules.service.admin.system.sysuser;

import org.springframework.stereotype.Service;

import web.backend.core.bases.BaseServiceImpl;
import web.backend.core.entitys.systems.SysUser;
import web.backend.modules.repository.admin.system.SysUserRepository;

@Service
public class SysUserServiceImpl extends BaseServiceImpl<SysUser, Long> implements SysUserService {

    public SysUserServiceImpl(SysUserRepository repository) {
        super(repository, repository, SysUser.class);
    }

}
