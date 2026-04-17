package com.vergium.core.engine;

import com.vergium.core.pipeline.VulkanFastPath;
import com.vergium.core.render.CommandBuffer;
import com.vergium.core.render.VergiumBatchRenderer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Dispatches custom meshes to the fast path when a GL context is available.
 */
public final class VergiumRenderDispatcher {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final VulkanFastPath VULKAN_PIPELINE = new VulkanFastPath();

    private VergiumRenderDispatcher() {
    }

    public static void dispatch(VergiumMeshBuilder mesh) {
        if (mesh == null || mesh.isEmpty()) {
            return;
        }

        VergiumBatchRenderer.getBufferForLayer("custom_engine_layer");

        if (!VULKAN_PIPELINE.isAvailable()) {
            return;
        }

        CommandBuffer commands = new CommandBuffer(1);
        commands.addCommand(mesh.getVertexCount(), 1, 0, 0);
        VULKAN_PIPELINE.setCommandBuffer(commands);

        try {
            VULKAN_PIPELINE.prepare();
            VULKAN_PIPELINE.render(0, mesh.getVertexCount());
            VULKAN_PIPELINE.finish();
        } catch (RuntimeException ex) {
            LOGGER.debug("Vergium fast-path dispatch skipped because the GL context was not ready.", ex);
        } finally {
            mesh.reset();
        }
    }
}
