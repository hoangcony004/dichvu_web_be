package web.backend.core.customs.responses;

import web.backend.core.dtos.systems.SysUserDTO;

public class TokenResponse {
    private String acces_token;
    // private String menu;
    private SysUserDTO user;

    public TokenResponse(String acces_token, SysUserDTO user) {
        this.acces_token = acces_token;
        // this.menu = menu;
        this.user = user;
    }

    public String getAccessToken() {
        return acces_token;
    }

    public void setAccessToken(String acces_token) {
        this.acces_token = acces_token;
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
