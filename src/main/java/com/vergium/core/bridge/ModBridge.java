package com.vergium.core.bridge;

import com.vergium.core.memory.BufferPool;
import com.vergium.core.render.VergiumBatchRenderer;
import java.nio.ByteBuffer;
import java.util.Locale;

/**
 * Minimal compatibility bridge that normalizes foreign render requests.
 */
public final class ModBridge {
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
        } finally {
            BufferPool.release(vertexData);
        }
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
