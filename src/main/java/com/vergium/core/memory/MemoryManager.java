package com.vergium.core.memory;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages off-heap memory allocations for vertex data.
 * Bypasses Java GC to prevent rendering stutters.
 */
public class MemoryManager {
    private static final List<ByteBuffer> ALLOCATED_BUFFERS = new ArrayList<>();

    /**
     * Allocates a direct buffer of the specified size.
     */
    public static ByteBuffer allocate(int size) {
        ByteBuffer buffer = ByteBuffer.allocateDirect(size).order(ByteOrder.nativeOrder());
        synchronized (ALLOCATED_BUFFERS) {
            ALLOCATED_BUFFERS.add(buffer);
        }
        return buffer;
    }

    /**
     * Clears all allocated buffers. Should be called on game shutdown or world change.
     */
    public static void freeAll() {
        synchronized (ALLOCATED_BUFFERS) {
            ALLOCATED_BUFFERS.clear();
            // System.gc() hint to clean up direct buffers if necessary, 
            // though manual management is preferred in advanced implementations.
            System.gc();
        }
    }
}
