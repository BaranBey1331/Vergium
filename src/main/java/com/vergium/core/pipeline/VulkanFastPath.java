package com.vergium.core.pipeline;

import com.vergium.core.render.CommandBuffer;
import org.lwjgl.opengl.GL31;
import org.lwjgl.opengl.GL40;
import org.lwjgl.opengl.GL43;

/**
 * FIXED: Truly implements Multi-Draw Indirect with dynamic draw counts.
 */
public class VulkanFastPath implements IRenderPipeline {
    private int indirectBufferId;
    private CommandBuffer activeCommands;

    @Override
    public void prepare() {
        if (indirectBufferId == 0) indirectBufferId = GL31.glGenBuffers();
        GL31.glBindBuffer(GL40.GL_DRAW_INDIRECT_BUFFER, indirectBufferId);
    }

    public void setCommandBuffer(CommandBuffer commands) {
        this.activeCommands = commands;
    }

    @Override
    public void render(int bufferId, int vertexCount) {
        if (activeCommands == null || activeCommands.getCommandCount() == 0) return;

        // Upload dynamic command data to GPU
        GL31.glBufferData(GL40.GL_DRAW_INDIRECT_BUFFER, activeCommands.getBuffer(), GL31.GL_STREAM_DRAW);

        // FIXED: Use actual dynamic command count instead of hardcoded '1'
        GL43.glMultiDrawArraysIndirect(GL31.GL_TRIANGLES, 0, activeCommands.getCommandCount(), 0);
    }

    @Override
    public void finish() {
        GL31.glBindBuffer(GL40.GL_DRAW_INDIRECT_BUFFER, 0);
    }
}
