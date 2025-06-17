package web.backend.core.auth.controller;

import java.util.List;

// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// import com.fasterxml.jackson.core.JsonProcessingException;
// import com.fasterxml.jackson.databind.ObjectMapper;
// import com.fasterxml.jackson.databind.node.ArrayNode;
// import com.fasterxml.jackson.databind.node.ObjectNode;

import web.backend.core.auth.service.JwtService;
import web.backend.core.customs.Constants;
import web.backend.core.customs.responses.ApiResponse;
import web.backend.core.customs.responses.TokenResponse;
import web.backend.core.dtos.systems.LoginRequestDTO;
import web.backend.core.dtos.systems.SysUserDTO;
// import web.backend.core.entitys.systems.SysMenu;
import web.backend.core.entitys.systems.SysUser;
import web.backend.modules.repository.system.SysUserRepository;

@RestController
@RequestMapping("/api")
public class AuthController {

    // private static final Logger logger =
    // LoggerFactory.getLogger(AuthController.class);

    private final JwtService jwtService;

    public AuthController(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Autowired
    private SysUserRepository sysUserRepository;

    // @Autowired
    // private SysRoleMenuRepository sysRoleMenuRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<TokenResponse>> login(@RequestBody LoginRequestDTO loginRequest) {
        String username = loginRequest.getUsername();
        String rawPassword = loginRequest.getPassword();
        // String menuUser = "";

        SysUser user = sysUserRepository.findByUsername(username);

        if (user == null || !passwordEncoder.matches(rawPassword, user.getPassword())) {
            ApiResponse<TokenResponse> response = new ApiResponse<>(
                    ApiResponse.Status.ERROR,
                    Constants.ApiMessage.LOGIN_FALSE,
                    Constants.ApiCode.UNAUTHORIZED);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
        // Lấy danh sách vai trò từ repository
        List<String> roles = sysUserRepository.findRolesByUsername(username);

        // Long userId = user.getId();
        // List<Object[]> menusWithPermission =
        // sysUserRepository.findMenuByUserId(userId);

        // ObjectMapper mapper = new ObjectMapper();
        // ArrayNode menuArray = mapper.createArrayNode();

        // if (!menusWithPermission.isEmpty()) {
        // for (Object[] result : menusWithPermission) {
        // SysMenu menu = (SysMenu) result[0];
        // String permissionType = (String) result[1];
        // String link = result.length > 2 ? (String) result[2] : "";
        // String icon = result.length > 3 ? (String) result[3] : "";
        // String label = result.length > 4 ? (String) result[4] : "";
        // Long parent = (result.length > 5 && result[5] != null) ? (Long) result[5] :
        // null;

        // ObjectNode menuNode = mapper.createObjectNode();
        // menuNode.put("id", menu.getId());
        // menuNode.put("label", label);
        // menuNode.put("link", link);
        // menuNode.put("icon", icon);
        // menuNode.put("permission", permissionType);
        // if (parent != null) {
        // menuNode.put("parent", parent);
        // } else {
        // menuNode.putNull("parent");
        // }

        // menuArray.add(menuNode);
        // }
        // }

        // try {
        // menuUser = mapper.writeValueAsString(menuArray);
        // } catch (JsonProcessingException e) {
        // System.err.println("Error while converting menuArray to JSON: " +
        // e.getMessage());
        // }
        // Tạo token với username và roles
        String token = jwtService.generateToken(user.getUsername(), roles, user.getUnitcode());
        SysUserDTO userDto = new SysUserDTO(user);
        TokenResponse tokenData = new TokenResponse(token, userDto);

        // Trả về kết quả thành công
        ApiResponse<TokenResponse> response = new ApiResponse<>(
                ApiResponse.Status.SUCCESS,
                Constants.ApiMessage.LOGIN_SUCCESS,
                Constants.ApiCode.OK,
                tokenData);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Object>> logout(@RequestBody String token) {

        try {
            // Loại bỏ dấu ngoặc kép và khoảng trắng thừa
            token = token.trim().replaceAll("^\"|\"$", "");

            // Loại bỏ tiền tố "Bearer " nếu có
            if (token.startsWith("Bearer ")) {
                token = token.substring(7);
            }

            String username = jwtService.extractUsername(token);
            jwtService.blacklistToken(token, username);

            ApiResponse<Object> response = new ApiResponse<>(
                    ApiResponse.Status.SUCCESS,
                    Constants.ApiMessage.LOGOUT_SUCCESS,
                    Constants.ApiCode.OK);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<Object> response = new ApiResponse<>(
                    ApiResponse.Status.ERROR,
                    "Lỗi khi đăng xuất: " + e.getMessage(),
                    Constants.ApiCode.INTERNAL_SERVER_ERROR);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

}
