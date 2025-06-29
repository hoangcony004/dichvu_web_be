package web.backend.core.auth.controller;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.models.responses.ApiResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import web.backend.core.auth.service.JwtService;
import web.backend.core.customs.Constants;
import web.backend.core.customs.responses.ApiResponseCustom;
import web.backend.core.customs.responses.TokenResponse;
import web.backend.core.dtos.systems.LoginRequestDTO;
import web.backend.core.dtos.systems.SysUserDTO;
import web.backend.core.entitys.systems.SysUser;
import web.backend.modules.repository.admin.system.sys_user.SysUserRepository;

@RestController
@RequestMapping("/api")
public class AuthController {
    private final JwtService jwtService;

    public AuthController(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Autowired
    private SysUserRepository sysUserRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<ApiResponseCustom<TokenResponse>> login(
            @RequestBody LoginRequestDTO loginRequest,
            HttpServletResponse response) {

        String username = loginRequest.getUsername();
        String rawPassword = loginRequest.getPassword();

        SysUser user = sysUserRepository.findByUsername(username);

        if (user == null || !passwordEncoder.matches(rawPassword, user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    new ApiResponseCustom<>(ApiResponseCustom.Status.ERROR, Constants.ApiMessage.LOGIN_FALSE,
                            Constants.ApiCode.UNAUTHORIZED));
        }

        List<String> roles = sysUserRepository.findRolesByUsername(username);

        String access_token = jwtService.generateAccessToken(user.getUsername(), roles, user.getUnitcode());
        String refresh_token = jwtService.generateRefreshToken(user.getUsername());

        // Ghi refresh token vào cookie
        Cookie cookie = new Cookie("refresh_token", refresh_token);
        cookie.setHttpOnly(true);
        cookie.setSecure(false); // nếu chưa dùng HTTPS, sau này nên set true
        cookie.setPath("/");
        cookie.setMaxAge((int) TimeUnit.DAYS.toSeconds(7));
        response.addCookie(cookie);

        SysUserDTO userDto = new SysUserDTO(user);
        TokenResponse tokenData = new TokenResponse(access_token, userDto);

        ApiResponseCustom<TokenResponse> responseBody = new ApiResponseCustom<>(ApiResponseCustom.Status.SUCCESS,
                Constants.ApiMessage.LOGIN_SUCCESS,
                Constants.ApiCode.OK,
                tokenData);

        return ResponseEntity.ok(responseBody);
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponseCustom<Object>> logout(@RequestBody String token) {

        try {
            // Loại bỏ dấu ngoặc kép và khoảng trắng thừa
            token = token.trim().replaceAll("^\"|\"$", "");

            // Loại bỏ tiền tố "Bearer " nếu có
            if (token.startsWith("Bearer ")) {
                token = token.substring(7);
            }

            String username = jwtService.extractUsername(token);
            jwtService.blacklistToken(token, username);

            ApiResponseCustom<Object> response = new ApiResponseCustom<>(
                    ApiResponseCustom.Status.SUCCESS,
                    Constants.ApiMessage.LOGOUT_SUCCESS,
                    Constants.ApiCode.OK);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponseCustom<Object> response = new ApiResponseCustom<>(
                    ApiResponseCustom.Status.ERROR,
                    "Lỗi khi đăng xuất: " + e.getMessage(),
                    Constants.ApiCode.INTERNAL_SERVER_ERROR);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/auth/refresh-token")
    public ResponseEntity<?> refreshToken(HttpServletRequest request) {
        String refreshToken = null;

        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("refresh_token".equals(cookie.getName())) {
                    refreshToken = cookie.getValue();
                    break;
                }
            }
        }

        if (refreshToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No refresh token found");
        }

        String username = jwtService.extractUsername(refreshToken);
        if (username == null || username.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token: no username found");
        }

        SysUser user = sysUserRepository.findByUsername(username);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");
        }

        List<String> roles = sysUserRepository.findRolesByUsername(username);
        // Bạn có thể lấy roles + unitCode từ DB nếu cần (để sinh lại access token)

        String newAccessToken = jwtService.generateAccessToken(username, roles, user.getUnitcode());

        return ResponseEntity.ok(Map.of("accessToken", newAccessToken));
    }

}
