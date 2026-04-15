package com.example.ratelimiter;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

// Sliding window log — exact rate limiting.
// Per key: a deque of request timestamps. On each call, evict timestamps older
// than (now - windowMs), then accept if remaining size < limit.
// O(n) time and space per key in the worst case, where n = limit.
public class SlidingWindowLogLimiter implements RateLimiter {
    private final int limit;
    private final long windowMs;
    private final Map<String, Deque<Long>> logs = new ConcurrentHashMap<>();

    public SlidingWindowLogLimiter(int limit, long windowMs) {
        if (limit <= 0) throw new IllegalArgumentException("limit must be positive");
        if (windowMs <= 0) throw new IllegalArgumentException("windowMs must be positive");
        this.limit = limit;
        this.windowMs = windowMs;
    }

    @Override
    public boolean tryAcquire(String key) {
        Deque<Long> log = logs.computeIfAbsent(key, k -> new ArrayDeque<>());
        long now = System.currentTimeMillis();
        long windowStart = now - windowMs;
        synchronized (log) {
            while (!log.isEmpty() && log.peekFirst() < windowStart) {
                log.pollFirst();
            }
            if (log.size() < limit) {
                log.offerLast(now);
                return true;
            }
            return false;
        }
    }
}
