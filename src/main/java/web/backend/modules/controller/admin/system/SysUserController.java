package web.backend.modules.controller.admin.system;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import web.backend.core.bases.BaseController;
import web.backend.core.entitys.systems.SysUser;
import web.backend.modules.service.admin.system.sysuser.SysUserService;

@RestController
@RequestMapping("/api/private/users")
public class SysUserController extends BaseController<SysUser, Long> {

    public SysUserController(SysUserService sysUserService) {
        super(sysUserService);
    }
}
