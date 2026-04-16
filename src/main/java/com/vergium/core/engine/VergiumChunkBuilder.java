package com.vergium.core.engine;

import com.vergium.core.render.VergiumBatchRenderer;

/**
 * Replaces vanilla Chunk Compilation logic.
 */
public class VergiumChunkBuilder {
    
    /**
     * Entry point for custom chunk building.
     */
    public static void build(int chunkX, int chunkY, int chunkZ) {
        VergiumMeshBuilder builder = new VergiumMeshBuilder(2 * 1024 * 1024); // 2MB
        
        // Custom Tesselation Logic here...
        // This is where we would iterate through blocks and call builder.vertex()
        
        // Send directly to the render dispatcher
        VergiumRenderDispatcher.dispatch(builder);
    }
}
