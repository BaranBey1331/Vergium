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

    private BufferPool() {
    }

    public static ByteBuffer acquire() {
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
            return;
        }

        buffer.clear();

        while (true) {
            int current = POOLED_COUNT.get();
            if (current >= MAX_POOLED_BUFFERS) {
                return;
            }
            if (POOLED_COUNT.compareAndSet(current, current + 1)) {
                POOL.offer(buffer);
                return;
            }
        }
    }

    public static int getPooledBufferCount() {
        return POOLED_COUNT.get();
    }

    public static void clear() {
        POOL.clear();
        POOLED_COUNT.set(0);
    }
}
