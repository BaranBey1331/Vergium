package com.vergium.core.memory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.ByteBuffer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

class NativeBufferTest {

    @AfterEach
    void tearDown() {
        MemoryManager.freeAll();
    }

    @Test
    void primitiveWritesAdvancePositionAndStoreValues() {
        NativeBuffer buffer = new NativeBuffer(16);

        buffer.putFloat(1.5f);
        buffer.putInt(7);
        buffer.putByte((byte) 3);
        buffer.putShort((short) 9);

        ByteBuffer bytes = buffer.getByteBuffer();
        assertEquals(11, buffer.getPosition());
        assertEquals(1.5f, bytes.getFloat(0));
        assertEquals(7, bytes.getInt(4));
        assertEquals(3, bytes.get(8));
        assertEquals(9, bytes.getShort(9));
    }

    @Test
    void packedUvClampsAndWritesTwoShorts() {
        NativeBuffer buffer = new NativeBuffer(4);

        buffer.putPackedUV(0.5f, 1.25f);

        ByteBuffer bytes = buffer.getByteBuffer();
        assertEquals(4, buffer.getPosition());
        assertEquals((short) Math.round(0.5f * 65535.0f), bytes.getShort(0));
        assertEquals((short) 65535, bytes.getShort(2));
    }

    @Test
    void packedPosWritesClampedSignedBytes() {
        NativeBuffer buffer = new NativeBuffer(3);

        buffer.putPackedPos(0.5f, -1.5f, 0.0f);

        ByteBuffer bytes = buffer.getByteBuffer();
        assertEquals(3, buffer.getPosition());
        assertEquals((byte) Math.round(0.5f * 127.0f), bytes.get(0));
        assertEquals((byte) -127, bytes.get(1));
        assertEquals((byte) 0, bytes.get(2));
    }

    @Test
    void bufferGrowsWhenCapacityIsExceeded() {
        NativeBuffer buffer = new NativeBuffer(4);

        buffer.putFloat(1.0f);
        buffer.putFloat(2.0f);

        assertTrue(buffer.getCapacity() >= 8);
        assertEquals(8, buffer.getPosition());
        assertEquals(1.0f, buffer.getByteBuffer().getFloat(0));
        assertEquals(2.0f, buffer.getByteBuffer().getFloat(4));
    }

    @Test
    void resetClearsPosition() {
        NativeBuffer buffer = new NativeBuffer(8);
        buffer.putInt(10);

        buffer.reset();

        assertEquals(0, buffer.getPosition());
        assertEquals(0, buffer.readableSlice().remaining());
    }
}
