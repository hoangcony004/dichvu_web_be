package web.backend.core.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import web.backend.core.configs.system_configuration.RateLimiterService;
import web.backend.core.configs.system_configuration.RequestQueueService;
import web.backend.core.customs.Constants.ApiCode;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import io.github.bucket4j.ConsumptionProbe;

import java.io.IOException;
import java.time.Duration;

@Component
@RequiredArgsConstructor
public class RateLimitInterceptor implements HandlerInterceptor {

    private final RateLimiterService rateLimiterService;
    private final RequestQueueService requestQueueService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
ConsumptionProbe probe = rateLimiterService.tryConsumeAndGetProbe();

if (probe.isConsumed()) {
    System.out.println("✅ Token còn: " + probe.getRemainingTokens());
    return true;
} else {
    // Ước tính thời gian chờ (giây)
    long nanosToWait = probe.getNanosToWaitForRefill();
    long secondsToWait = Duration.ofNanos(nanosToWait).getSeconds();

    String name = request.getParameter("name");
    if (name == null) name = "anonymous";
    requestQueueService.enqueue(name);

    response.setStatus(ApiCode.TOO_MANY_REQUESTS);
    response.getWriter().write(
        "Truy cập đang tăng. Đã thêm vào hàng đợi: "
        + requestQueueService.size()
        + ". Thời gian chờ dự kiến: "
        + secondsToWait + " giây."
    );
    return false;
}

    }
}
