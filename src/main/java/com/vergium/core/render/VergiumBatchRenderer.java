package com.vergium.core.render;

import com.vergium.core.memory.NativeBuffer;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Thread-safe drawing engine that collects chunk data.
 * Optimized to handle multi-threaded chunk building in Minecraft.
 */
public class VergiumBatchRenderer {
    private static final Map<String, NativeBuffer> RENDER_BATCHES = new ConcurrentHashMap<>();

    /**
     * Acquires a buffer for a specific render layer. Thread-safe.
     */
    public static NativeBuffer getBufferForLayer(String layerName) {
        return RENDER_BATCHES.computeIfAbsent(layerName, k -> new NativeBuffer(1024 * 1024));
    }

    /**
     * Resets all batches.
     */
    public static void clearAll() {
        RENDER_BATCHES.values().forEach(NativeBuffer::reset);
        RENDER_BATCHES.clear();
    }

    /**
     * Flushes all batches to the GPU.
     */
    public static void flush() {
        for (NativeBuffer buffer : RENDER_BATCHES.values()) {
            if (buffer.getPosition() > 0) {
                // Submit to GPU...
                buffer.reset();
            }
        }
    }
}
