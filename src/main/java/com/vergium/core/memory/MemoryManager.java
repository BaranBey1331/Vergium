package com.vergium.core.memory;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Tracks direct buffer allocations used by Vergium's native-style data paths.
 *
 * <p>The JVM owns the actual reclamation of direct buffers, so this manager focuses on
 * deterministic tracking/visibility instead of forcing global GC cycles.
 */
public final class MemoryManager {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final Object LOCK = new Object();
    private static final Set<ByteBuffer> ALLOCATED_BUFFERS =
            Collections.newSetFromMap(new IdentityHashMap<>());
    private static final AtomicLong TRACKED_BYTES = new AtomicLong();
    private static final AtomicLong PEAK_TRACKED_BYTES = new AtomicLong();

    private MemoryManager() {
    }

    public static ByteBuffer allocate(int size) {
        if (size <= 0) {
            throw new IllegalArgumentException("Allocation size must be positive");
        }

        ByteBuffer buffer = ByteBuffer.allocateDirect(size).order(ByteOrder.nativeOrder());
        synchronized (LOCK) {
            ALLOCATED_BUFFERS.add(buffer);
        }
        long tracked = TRACKED_BYTES.addAndGet(size);
        PEAK_TRACKED_BYTES.accumulateAndGet(tracked, Math::max);
        return buffer;
    }

    public static void free(ByteBuffer buffer) {
        if (buffer == null) {
            return;
        }

        boolean removed;
        synchronized (LOCK) {
            removed = ALLOCATED_BUFFERS.remove(buffer);
        }

        if (removed) {
            TRACKED_BYTES.addAndGet(-buffer.capacity());
        }
    }

    public static void freeAll() {
        int count;
        long bytes;
        synchronized (LOCK) {
            count = ALLOCATED_BUFFERS.size();
            bytes = TRACKED_BYTES.get();
            ALLOCATED_BUFFERS.clear();
        }
        TRACKED_BYTES.set(0L);
        LOGGER.info("Cleared {} tracked direct buffers ({} bytes).", count, bytes);
    }

    /**
     * Backwards-compatible accessor that now returns a defensive snapshot.
     */
    public static List<ByteBuffer> getAllocatedBuffers() {
        synchronized (LOCK) {
            return new ArrayList<>(ALLOCATED_BUFFERS);
        }
    }

    public static int getTrackedBufferCount() {
        synchronized (LOCK) {
            return ALLOCATED_BUFFERS.size();
        }
    }

    public static long getTrackedBytes() {
        return TRACKED_BYTES.get();
    }

    public static long getPeakTrackedBytes() {
        return PEAK_TRACKED_BYTES.get();
    }
}
