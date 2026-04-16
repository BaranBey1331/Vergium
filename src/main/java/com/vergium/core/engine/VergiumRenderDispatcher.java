package com.vergium.core.engine;

import com.vergium.core.render.VergiumBatchRenderer;
import com.vergium.core.pipeline.VulkanFastPath;

/**
 * FIXED: Removed invalid PipelineManager import.
 * Manages the high-performance dispatch of custom meshes to the GPU.
 */
public class VergiumRenderDispatcher {
    private static final VulkanFastPath VULKAN_PIPELINE = new VulkanFastPath();
    
    /**
     * Dispatches a custom mesh directly to the optimized pipeline.
     */
    public static void dispatch(VergiumMeshBuilder mesh) {
        if (mesh.getBuffer().getPosition() > 0) {
            // Use the established batch renderer for cross-layer optimization
            VergiumBatchRenderer.getBufferForLayer("custom_engine_layer");
            
            // Prepare and execute via Vulkan Fast Path
            VULKAN_PIPELINE.prepare();
            VULKAN_PIPELINE.render(0, mesh.getBuffer().getPosition() / VertexFormat.STRIDE);
            VULKAN_PIPELINE.finish();
        }
    }
}
