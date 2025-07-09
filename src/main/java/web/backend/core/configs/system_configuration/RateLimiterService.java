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
                500,
                Refill.greedy(500, Duration.ofMinutes(1))
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
