package com.vergium.core.render;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

/**
 * CPU-side store for draw-indirect commands.
 */
public final class CommandBuffer {
    public static final int COMMAND_STRIDE_BYTES = 16;

    private ByteBuffer buffer;
    private int commandCount;

    public CommandBuffer(int initialCapacity) {
        if (initialCapacity <= 0) {
            throw new IllegalArgumentException("Initial capacity must be positive");
        }
        this.buffer = ByteBuffer.allocateDirect(initialCapacity * COMMAND_STRIDE_BYTES)
                .order(ByteOrder.nativeOrder());
    }

    public void addCommand(int count, int instanceCount, int first, int baseInstance) {
        ensureCapacity(COMMAND_STRIDE_BYTES);
        buffer.putInt(count);
        buffer.putInt(instanceCount);
        buffer.putInt(first);
        buffer.putInt(baseInstance);
        commandCount++;
    }

    public void reset() {
        buffer.clear();
        commandCount = 0;
    }

    public int getCommandCount() {
        return commandCount;
    }

    public boolean isEmpty() {
        return commandCount == 0;
    }

    public ByteBuffer getBuffer() {
        ByteBuffer duplicate = buffer.duplicate().order(ByteOrder.nativeOrder());
        duplicate.flip();
        return duplicate;
    }

    public List<int[]> snapshotCommands() {
        ByteBuffer readable = getBuffer();
        List<int[]> commands = new ArrayList<>(commandCount);
        while (readable.remaining() >= COMMAND_STRIDE_BYTES) {
            commands.add(new int[] {
                    readable.getInt(),
                    readable.getInt(),
                    readable.getInt(),
                    readable.getInt()
            });
        }
        return commands;
    }

    private void ensureCapacity(int bytesNeeded) {
        if (buffer.remaining() >= bytesNeeded) {
            return;
        }

        int newCapacity = buffer.capacity();
        int requiredCapacity = buffer.position() + bytesNeeded;
        while (newCapacity < requiredCapacity) {
            newCapacity *= 2;
        }

        ByteBuffer replacement = ByteBuffer.allocateDirect(newCapacity).order(ByteOrder.nativeOrder());
        buffer.flip();
        replacement.put(buffer);
        buffer = replacement;
    }
}
