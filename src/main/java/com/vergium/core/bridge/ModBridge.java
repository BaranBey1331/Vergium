package com.vergium.core.bridge;

import com.vergium.core.memory.BufferPool;
import com.vergium.core.render.VergiumBatchRenderer;
import java.nio.ByteBuffer;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Minimal compatibility bridge that normalizes foreign render requests.
 */
public final class ModBridge {
    private static final Map<String, AtomicInteger> BRIDGED_VERTICES = new ConcurrentHashMap<>();

    private ModBridge() {
    }

    public static void bridgeDraw(String modId, int vertexCount) {
        String normalizedModId = sanitizeModId(modId);
        String layerKey = "mod_layer_" + normalizedModId;

        ByteBuffer vertexData = BufferPool.acquire();
        try {
            if (vertexCount > 0) {
                vertexData.putInt(vertexCount);
                vertexData.flip();
            }
            VergiumBatchRenderer.getBufferForLayer(layerKey);
            BRIDGED_VERTICES.computeIfAbsent(normalizedModId, ignored -> new AtomicInteger()).addAndGet(Math.max(0, vertexCount));
        } finally {
            BufferPool.release(vertexData);
        }
    }

    public static int getBridgedVertexCount(String modId) {
        return BRIDGED_VERTICES.getOrDefault(sanitizeModId(modId), new AtomicInteger()).get();
    }

    public static void resetStats() {
        BRIDGED_VERTICES.clear();
    }

    private static String sanitizeModId(String modId) {
        if (modId == null || modId.isBlank()) {
            return "unknown";
        }

        return modId.trim()
                .toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9_.-]", "_");
    }
}
