package com.vergium.core.memory;

import java.nio.ByteBuffer;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Reuses DirectByteBuffers to eliminate allocations and leaks.
 */
public class BufferPool {
    private static final Queue<ByteBuffer> POOL = new ConcurrentLinkedQueue<>();
    private static final int BUFFER_SIZE = 2 * 1024 * 1024; // 2MB per buffer

    /**
     * Borrows a buffer from the pool or creates a new one if empty.
     */
    public static ByteBuffer acquire() {
        ByteBuffer buffer = POOL.poll();
        if (buffer == null) {
            buffer = ByteBuffer.allocateDirect(BUFFER_SIZE);
        }
        buffer.clear();
        return buffer;
    }

    /**
     * Returns a buffer to the pool for later reuse.
     */
    public static void release(ByteBuffer buffer) {
        if (POOL.size() < 64) { // Limit pool size to 128MB max
            POOL.add(buffer);
        }
    }
}
