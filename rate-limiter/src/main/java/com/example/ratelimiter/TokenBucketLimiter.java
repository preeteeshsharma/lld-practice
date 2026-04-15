package com.example.ratelimiter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

// Token bucket with lazy refill — no background thread.
// Each call computes tokens accumulated since lastRefill using elapsed nanos.
// O(1) time and space per key. Allows bursts up to `capacity`.
public class TokenBucketLimiter implements RateLimiter {
    private final double capacity;
    private final double refillRatePerSec;
    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

    public TokenBucketLimiter(double capacity, double refillRatePerSec) {
        if (capacity <= 0) throw new IllegalArgumentException("capacity must be positive");
        if (refillRatePerSec <= 0) throw new IllegalArgumentException("refillRate must be positive");
        this.capacity = capacity;
        this.refillRatePerSec = refillRatePerSec;
    }

    @Override
    public boolean tryAcquire(String key) {
        Bucket bucket = buckets.computeIfAbsent(key, k -> new Bucket(capacity, System.nanoTime()));
        synchronized (bucket) {
            long now = System.nanoTime();
            double elapsedSec = (now - bucket.lastRefillNanos) / 1_000_000_000.0;
            bucket.tokens = Math.min(capacity, bucket.tokens + elapsedSec * refillRatePerSec);
            bucket.lastRefillNanos = now;
            if (bucket.tokens >= 1.0) {
                bucket.tokens -= 1.0;
                return true;
            }
            return false;
        }
    }

    private static final class Bucket {
        double tokens;
        long lastRefillNanos;

        Bucket(double tokens, long lastRefillNanos) {
            this.tokens = tokens;
            this.lastRefillNanos = lastRefillNanos;
        }
    }
}
