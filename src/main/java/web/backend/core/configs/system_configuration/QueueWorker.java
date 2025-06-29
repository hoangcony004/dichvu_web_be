package web.backend.core.configs.system_configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QueueWorker {

    private final RateLimiterService rateLimiterService;
    private final RequestQueueService requestQueueService;

    @Scheduled(fixedDelay = 100)
    public void processQueue() {
        if (rateLimiterService.tryConsume()) {
            String req = requestQueueService.dequeue();
            if (req != null) {
                System.out.println("👉 Đang xử lý từ queue: " + req);
                // TODO: Thực hiện xử lý thật sự ở đây
            }
        }
    }
}
