package com.vergium.core.render;

import com.vergium.core.memory.NativeBuffer;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Thread-safe staging area for per-layer geometry.
 */
public final class VergiumBatchRenderer {
    private static final int DEFAULT_LAYER_CAPACITY = 1024 * 1024;

    private static final ConcurrentHashMap<String, NativeBuffer> RENDER_BATCHES = new ConcurrentHashMap<>();
    private static final AtomicInteger FLUSH_COUNT = new AtomicInteger();

    private VergiumBatchRenderer() {
    }

    public static NativeBuffer getBufferForLayer(String layerName) {
        String normalized = normalizeLayerName(layerName);
        return RENDER_BATCHES.computeIfAbsent(normalized, key -> new NativeBuffer(DEFAULT_LAYER_CAPACITY));
    }

    public static int flush() {
        int flushedLayers = 0;
        for (NativeBuffer buffer : RENDER_BATCHES.values()) {
            if (!buffer.isEmpty()) {
                buffer.reset();
                flushedLayers++;
            }
        }
        if (flushedLayers > 0) {
            FLUSH_COUNT.addAndGet(flushedLayers);
        }
        return flushedLayers;
    }

    public static void clearAll() {
        RENDER_BATCHES.values().forEach(NativeBuffer::reset);
        RENDER_BATCHES.clear();
    }

    public static int getBatchCount() {
        return RENDER_BATCHES.size();
    }

    public static int getFlushCount() {
        return FLUSH_COUNT.get();
    }

    public static Set<String> getTrackedLayers() {
        return Set.copyOf(RENDER_BATCHES.keySet());
    }

    private static String normalizeLayerName(String layerName) {
        if (layerName == null || layerName.isBlank()) {
            return "default";
        }
        return layerName.trim().toLowerCase();
    }
}
