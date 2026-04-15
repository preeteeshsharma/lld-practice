package com.example.ratelimiter;

// Strategy pattern — swap the algorithm behind this interface without touching callers.
// Impls: TokenBucketLimiter, SlidingWindowLogLimiter. Others plug in the same way
// (fixed-window counter, leaky bucket, distributed Redis-backed limiter).
public interface RateLimiter {
    boolean tryAcquire(String key);
}
