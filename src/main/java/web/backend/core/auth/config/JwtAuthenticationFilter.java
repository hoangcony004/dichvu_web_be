package web.backend.core.auth.config;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import web.backend.core.auth.service.JwtService;
import web.backend.core.customs.responses.ApiResponseCustom;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        String path = request.getRequestURI();

        // Bỏ qua những API công khai
        if (path.startsWith("/api/login")
                || path.startsWith("/api/logout")
                || path.startsWith("/api/auth/refresh-token")
                || path.startsWith("/swagger")
                || path.startsWith("/v3/api-docs")
                || path.startsWith("/public")) {
            chain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            try {
                if (jwtService.isValidToken(token)) {
                    String username = jwtService.extractUsername(token);
                    List<String> roles = jwtService.extractRoles(token);

                    List<GrantedAuthority> authorities = roles.stream()
                            .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                            .collect(Collectors.toList());

                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            username, null, authorities);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } else {
                    sendErrorResponse(response, "Phiên đăng nhập hết hạn.");
                    return;
                }
            } catch (Exception e) {
                sendErrorResponse(response, "Token không hợp lệ: " + e.getMessage());
                return;
            }
        } else {
            sendErrorResponse(response, "Thiếu token.");
            return;
        }

        chain.doFilter(request, response);
    }

    private void sendErrorResponse(HttpServletResponse res, String message) throws IOException {
        ApiResponseCustom<Object> apiResponse = new ApiResponseCustom<>(
                ApiResponseCustom.Status.ERROR,
                message,
                HttpServletResponse.SC_UNAUTHORIZED);

        res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");

        ObjectMapper mapper = new ObjectMapper();
        res.getWriter().write(mapper.writeValueAsString(apiResponse));
    }

}
