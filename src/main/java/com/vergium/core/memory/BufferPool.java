package com.vergium.core.memory;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Reuses fixed-size direct buffers to reduce allocation churn on transient bridges.
 */
public final class BufferPool {
    public static final int BUFFER_SIZE = 2 * 1024 * 1024;
    public static final int MAX_POOLED_BUFFERS = 64;

    private static final Queue<ByteBuffer> POOL = new ConcurrentLinkedQueue<>();
    private static final AtomicInteger POOLED_COUNT = new AtomicInteger();
    private static final AtomicInteger TOTAL_ACQUIRES = new AtomicInteger();
    private static final AtomicInteger TOTAL_RELEASES = new AtomicInteger();
    private static final AtomicInteger TOTAL_REJECTIONS = new AtomicInteger();

    private BufferPool() {
    }

    public static ByteBuffer acquire() {
        TOTAL_ACQUIRES.incrementAndGet();
        ByteBuffer buffer = POOL.poll();
        if (buffer != null) {
            POOLED_COUNT.decrementAndGet();
            buffer.clear();
            return buffer;
        }

        return ByteBuffer.allocateDirect(BUFFER_SIZE).order(ByteOrder.nativeOrder());
    }

    public static void release(ByteBuffer buffer) {
        if (buffer == null || !buffer.isDirect() || buffer.capacity() != BUFFER_SIZE) {
            TOTAL_REJECTIONS.incrementAndGet();
            return;
        }

        buffer.clear();

        while (true) {
            int current = POOLED_COUNT.get();
            if (current >= MAX_POOLED_BUFFERS) {
                TOTAL_REJECTIONS.incrementAndGet();
                return;
            }
            if (POOLED_COUNT.compareAndSet(current, current + 1)) {
                TOTAL_RELEASES.incrementAndGet();
                POOL.offer(buffer);
                return;
            }
        }
    }

    public static int getPooledBufferCount() {
        return POOLED_COUNT.get();
    }

    public static int getTotalAcquires() {
        return TOTAL_ACQUIRES.get();
    }

    public static int getTotalReleases() {
        return TOTAL_RELEASES.get();
    }

    public static int getTotalRejections() {
        return TOTAL_REJECTIONS.get();
    }

    public static void clear() {
        POOL.clear();
        POOLED_COUNT.set(0);
        TOTAL_ACQUIRES.set(0);
        TOTAL_RELEASES.set(0);
        TOTAL_REJECTIONS.set(0);
    }
}
