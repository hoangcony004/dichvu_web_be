package web.backend.core.customs.responses;

import web.backend.core.dtos.systems.SysUserDTO;

public class TokenResponse {
    private String token;
    // private String menu;
    private SysUserDTO user;

    public TokenResponse(String token, SysUserDTO user) {
        this.token = token;
        // this.menu = menu;
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    // public String getMenu() {
    //     return menu;
    // }

    // public void setMenu(String menu) {
    //     this.menu = menu;
    // }

    public SysUserDTO getUser() {
        return user;
    }

    public void setUser(SysUserDTO user) {
        this.user = user;
    }
}
