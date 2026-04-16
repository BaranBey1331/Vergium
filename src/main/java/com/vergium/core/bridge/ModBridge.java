package com.vergium.core.bridge;

import com.vergium.core.memory.BufferPool;
import com.vergium.core.render.VergiumBatchRenderer;
import java.nio.ByteBuffer;

/**
 * Acts as a bridge between other mods and Vergium's optimized pipeline.
 */
public class ModBridge {
    
    /**
     * Intercepts a mod's draw data and routes it into the optimized path.
     */
    public static void bridgeDraw(String modId, int vertexCount) {
        ByteBuffer optimizedBuffer = BufferPool.acquire();
        try {
            // Processing logic to batch this mod's data...
            VergiumBatchRenderer.getBufferForLayer("mod_" + modId);
        } finally {
            BufferPool.release(optimizedBuffer);
        }
    }
}
