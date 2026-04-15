package com.example.ratelimiter;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Token Bucket: capacity=5, refill=2/sec ===");
        RateLimiter bucket = new TokenBucketLimiter(5, 2);
        for (int i = 1; i <= 8; i++) {
            System.out.printf("req %d: %s%n", i, bucket.tryAcquire("partner-A") ? "ALLOWED" : "BLOCKED");
        }
        System.out.println("-- sleep 1s (~2 tokens refill) --");
        Thread.sleep(1000);
        for (int i = 9; i <= 12; i++) {
            System.out.printf("req %d: %s%n", i, bucket.tryAcquire("partner-A") ? "ALLOWED" : "BLOCKED");
        }

        System.out.println("\n=== Sliding Window Log: limit=3 per 1000ms ===");
        RateLimiter window = new SlidingWindowLogLimiter(3, 1000);
        for (int i = 1; i <= 5; i++) {
            System.out.printf("req %d: %s%n", i, window.tryAcquire("partner-A") ? "ALLOWED" : "BLOCKED");
        }
        System.out.println("-- sleep 1100ms (window slides) --");
        Thread.sleep(1100);
        for (int i = 6; i <= 8; i++) {
            System.out.printf("req %d: %s%n", i, window.tryAcquire("partner-A") ? "ALLOWED" : "BLOCKED");
        }

        System.out.println("\n=== Per-key isolation: partner-B has its own bucket ===");
        System.out.printf("partner-B req 1: %s%n", bucket.tryAcquire("partner-B") ? "ALLOWED" : "BLOCKED");
    }
}
