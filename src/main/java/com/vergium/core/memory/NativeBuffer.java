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
