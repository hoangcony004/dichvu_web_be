package web.backend.core.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import web.backend.core.entitys.systems.SysToken;
import web.backend.core.entitys.systems.SysUser;
import web.backend.modules.repository.admin.system.SysTokenRepository;
import web.backend.modules.repository.admin.system.SysUserRepository;

import java.sql.Timestamp;

@Service
public class TokenBlacklistService {
    
    @Autowired
    private SysTokenRepository sysTokenRepository;

    @Autowired
    private SysUserRepository sysUserRepository;

    public void blacklistToken(String token, String username) {
        SysUser user = sysUserRepository.findByUsername(username);
        
        SysToken sysToken = new SysToken();
        sysToken.setToken(token);
        sysToken.setIsRevoked(true);
        sysToken.setUser(user);
        sysToken.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        sysTokenRepository.save(sysToken);
    }
    
    public boolean isTokenBlacklisted(String token) {
        return sysTokenRepository.existsByTokenAndIsRevokedTrue(token);
    }
}
