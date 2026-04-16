package com.vergium.core.memory;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Manages off-heap memory allocations for vertex data.
 * Bypasses Java GC to prevent rendering stutters.
 */
public class MemoryManager {
    private static final Logger LOGGER = LogManager.getLogger();
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
            int count = ALLOCATED_BUFFERS.size();
            ALLOCATED_BUFFERS.clear();
            // System.gc() hint to clean up direct buffers if necessary
            System.gc();
            System.runFinalization();
            LOGGER.info("Cleared {} off-heap buffers from MemoryManager.", count);
        }
    }

    /**
     * Returns the list of allocated buffers for external management.
     */
    public static List<ByteBuffer> getAllocatedBuffers() {
        return ALLOCATED_BUFFERS;
    }
}
