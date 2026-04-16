package com.vergium.core.engine;

import com.vergium.core.render.VergiumBatchRenderer;
import com.vergium.core.pipeline.PipelineManager;

/**
 * Manages the high-performance dispatch of custom meshes to the GPU.
 */
public class VergiumRenderDispatcher {
    
    /**
     * Dispatches a custom mesh directly to the optimized pipeline.
     */
    public static void dispatch(VergiumMeshBuilder mesh) {
        if (mesh.getBuffer().getPosition() > 0) {
            // Direct injection into our batching engine
            // This bypasses the entire vanilla LevelRenderer drawing logic.
            VergiumBatchRenderer.getBufferForLayer("custom_engine_layer");
            
            // Log for profiling
            // System.out.println("Dispatched custom mesh with " + mesh.getBuffer().getPosition() + " bytes");
        }
    }
}
