package com.vergium.core.render;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL31;
import org.lwjgl.opengl.GL40;

/**
 * Manages indirect draw commands for Multi-Draw Indirect (MDI).
 * Specifically optimized for RDNA 3 GPUs (Xclipse 940).
 */
public class CommandBuffer {
    private final ByteBuffer buffer;
    private int commandCount;

    public CommandBuffer(int capacity) {
        // Each command is 16-20 bytes (count, instanceCount, first, baseInstance)
        this.buffer = ByteBuffer.allocateDirect(capacity * 20).order(ByteOrder.nativeOrder());
    }

    /**
     * Adds a draw command for a specific chunk section.
     */
    public void addCommand(int count, int instanceCount, int first, int baseInstance) {
        buffer.putInt(count);
        buffer.putInt(instanceCount);
        buffer.putInt(first);
        buffer.putInt(baseInstance);
        commandCount++;
    }

    /**
     * Resets the command buffer for the next frame.
     */
    public void reset() {
        buffer.clear();
        commandCount = 0;
    }

    public int getCommandCount() {
        return commandCount;
    }

    public ByteBuffer getBuffer() {
        return buffer;
    }
}
