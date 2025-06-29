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
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String path = req.getRequestURI();
        if (path.startsWith("/api/private/")) {
            String authHeader = req.getHeader("Authorization");
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);
                try {
                    if (jwtService.isValidToken(token)) {
                        System.out.println("✅ Token hop le");
                        String username = jwtService.extractUsername(token);
                        List<String> roles = jwtService.extractRoles(token);

                        List<GrantedAuthority> authorities = roles.stream()
                                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                                .collect(Collectors.toList());

                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                                username, null, authorities);
                        SecurityContextHolder.getContext().setAuthentication(authentication);

                        chain.doFilter(request, response);
                        return;
                    } else {
                        ApiResponseCustom<Object> apiResponse = new ApiResponseCustom<>(
                                ApiResponseCustom.Status.ERROR,
                                "Phiên đăng nhập hết hạn.",
                                HttpServletResponse.SC_UNAUTHORIZED);

                        res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        res.setContentType("application/json");
                        res.setCharacterEncoding("UTF-8");

                        ObjectMapper mapper = new ObjectMapper();
                        res.getWriter().write(mapper.writeValueAsString(apiResponse));

                    }

                } catch (Exception e) {
                    res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    res.getWriter().write("Token khong hop le: " + e.getMessage());
                }
            } else {
                res.setStatus(HttpServletResponse.SC_FORBIDDEN);
                res.getWriter().write("Thieu token.");
            }
        } else {
            chain.doFilter(request, response);
        }
    }
}
