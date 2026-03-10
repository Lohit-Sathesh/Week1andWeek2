import java.util.HashMap;

class TokenBucket {

    int tokens;
    int maxTokens;
    double refillRate;
    long lastRefillTime;

    TokenBucket(int maxTokens, double refillRate) {
        this.tokens = maxTokens;
        this.maxTokens = maxTokens;
        this.refillRate = refillRate;
        this.lastRefillTime = System.currentTimeMillis();
    }

    synchronized boolean allowRequest() {

        refill();

        if (tokens > 0) {
            tokens--;
            return true;
        }

        return false;
    }

    void refill() {

        long now = System.currentTimeMillis();

        double tokensToAdd =
                (now - lastRefillTime) / 1000.0 * refillRate;

        tokens = Math.min(maxTokens,
                tokens + (int) tokensToAdd);

        lastRefillTime = now;
    }
}

public class RateLimiter {

    HashMap<String, TokenBucket> clients = new HashMap<>();

    int MAX_REQUESTS = 1000;

    double REFILL_RATE = 1000.0 / 3600;

    public String checkRateLimit(String clientId) {

        clients.putIfAbsent(clientId,
                new TokenBucket(MAX_REQUESTS, REFILL_RATE));

        TokenBucket bucket = clients.get(clientId);

        if (bucket.allowRequest()) {

            return "Allowed (" + bucket.tokens + " remaining)";
        }

        return "Denied (rate limit exceeded)";
    }

    public static void main(String[] args) {

        RateLimiter limiter = new RateLimiter();

        System.out.println(limiter.checkRateLimit("abc123"));
        System.out.println(limiter.checkRateLimit("abc123"));
    }
}