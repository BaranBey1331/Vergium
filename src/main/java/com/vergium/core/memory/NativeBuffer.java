package com.vergium.core.memory;

import java.nio.ByteBuffer;

/**
 * A specialized wrapper for native memory buffers to provide fast vertex data packing.
 */
public class NativeBuffer {
    private final ByteBuffer buffer;
    private int position;

    public NativeBuffer(int initialSize) {
        this.buffer = MemoryManager.allocate(initialSize);
        this.position = 0;
    }

    public void putFloat(float f) {
        buffer.putFloat(position, f);
        position += 4;
    }

    public void putInt(int i) {
        buffer.putInt(position, i);
        position += 4;
    }

    public void putByte(byte b) {
        buffer.put(position, b);
        position += 1;
    }

    public void putShort(short s) {
        buffer.putShort(position, s);
        position += 2;
    }

    /**
     * Packs two float values into a single integer using short precision.
     * Efficient for UV coordinates to save bandwidth.
     */
    public void putPackedUV(float u, float v) {
        short su = (short) (u * 65535.0f);
        short sv = (short) (v * 65535.0f);
        buffer.putShort(position, su);
        buffer.putShort(position + 2, sv);
        position += 4;
    }

    /**
     * Packs three float coordinates into byte values if they are within a small range.
     * Used for local block offsets.
     */
    public void putPackedPos(float x, float y, float z) {
        buffer.put(position, (byte) (x * 127.0f));
        buffer.put(position + 1, (byte) (y * 127.0f));
        buffer.put(position + 2, (byte) (z * 127.0f));
        position += 3;
    }

    /**
     * Resets the buffer position for the next frame or batch.
     */
    public void reset() {
        position = 0;
        buffer.clear();
    }

    public ByteBuffer getByteBuffer() {
        return buffer;
    }

    public int getPosition() {
        return position;
    }

    public int getCapacity() {
        return buffer.capacity();
    }
}
