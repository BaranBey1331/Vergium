package com.vergium.core.render;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.ByteBuffer;
import java.util.List;
import org.junit.jupiter.api.Test;

class CommandBufferTest {

    @Test
    void commandSnapshotPreservesLayout() {
        CommandBuffer buffer = new CommandBuffer(1);
        buffer.addCommand(6, 1, 2, 3);

        List<int[]> commands = buffer.snapshotCommands();

        assertEquals(1, buffer.getCommandCount());
        assertEquals(1, commands.size());
        assertArrayEquals(new int[] {6, 1, 2, 3}, commands.get(0));
    }

    @Test
    void getBufferReturnsReadableView() {
        CommandBuffer buffer = new CommandBuffer(1);
        buffer.addCommand(4, 5, 6, 7);

        ByteBuffer readable = buffer.getBuffer();

        assertEquals(CommandBuffer.COMMAND_STRIDE_BYTES, readable.remaining());
        assertEquals(4, readable.getInt());
        assertEquals(5, readable.getInt());
        assertEquals(6, readable.getInt());
        assertEquals(7, readable.getInt());
    }
}
