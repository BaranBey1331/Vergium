package com.vergium.core.pipeline;

import com.vergium.core.render.CommandBuffer;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL31;
import org.lwjgl.opengl.GL40;
import org.lwjgl.opengl.GL43;

/**
 * OpenGL-backed fast path that gracefully disables itself without a live context.
 */
public final class VulkanFastPath implements IRenderPipeline {
    private int indirectBufferId;
    private CommandBuffer activeCommands;
    private boolean prepared;

    @Override
    public void prepare() {
        if (!isAvailable()) {
            prepared = false;
            return;
        }

        if (indirectBufferId == 0) {
            indirectBufferId = GL31.glGenBuffers();
        }
        GL31.glBindBuffer(GL40.GL_DRAW_INDIRECT_BUFFER, indirectBufferId);
        prepared = true;
    }

    public void setCommandBuffer(CommandBuffer commands) {
        this.activeCommands = commands;
    }

    public boolean isAvailable() {
        try {
            return GL.getCapabilities() != null;
        } catch (IllegalStateException ex) {
            return false;
        }
    }

    @Override
    public void render(int bufferId, int vertexCount) {
        if (!prepared || vertexCount <= 0) {
            return;
        }

        if (activeCommands != null && !activeCommands.isEmpty()) {
            GL31.glBufferData(GL40.GL_DRAW_INDIRECT_BUFFER, activeCommands.getBuffer(), GL31.GL_STREAM_DRAW);
            GL43.glMultiDrawArraysIndirect(GL11.GL_TRIANGLES, 0, activeCommands.getCommandCount(), 0);
        }
    }

    @Override
    public void finish() {
        if (prepared) {
            GL31.glBindBuffer(GL40.GL_DRAW_INDIRECT_BUFFER, 0);
        }
        prepared = false;
    }
}
