package web.backend.core.configs.system_configuration;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.ConsumptionProbe;
import io.github.bucket4j.Refill;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class RateLimiterService {

    private final Bucket bucket;

    public RateLimiterService() {
        Bandwidth limit = Bandwidth.classic(
                300,                              // Quota: 300 request
                Refill.greedy(300, Duration.ofMinutes(1))  // Tự refill 300 token mỗi phút
        );
        this.bucket = Bucket4j.builder()
                .addLimit(limit)
                .build();
    }

    public boolean tryConsume() {
        return bucket.tryConsume(1);
    }

    public long getAvailableTokens() {
        return bucket.getAvailableTokens();
    }

    public ConsumptionProbe tryConsumeAndGetProbe() {
    return bucket.tryConsumeAndReturnRemaining(1);
}
}
