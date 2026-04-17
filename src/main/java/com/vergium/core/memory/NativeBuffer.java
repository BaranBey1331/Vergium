package com.vergium.core.memory;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Growable native buffer wrapper with deterministic write offsets.
 */
public final class NativeBuffer {
    private ByteBuffer buffer;
    private int position;

    public NativeBuffer(int initialSize) {
        if (initialSize <= 0) {
            throw new IllegalArgumentException("Initial size must be positive");
        }
        this.buffer = MemoryManager.allocate(initialSize);
        this.position = 0;
    }

    public void putFloat(float value) {
        ensureWritable(Float.BYTES);
        buffer.putFloat(position, value);
        position += Float.BYTES;
    }

    public void putInt(int value) {
        ensureWritable(Integer.BYTES);
        buffer.putInt(position, value);
        position += Integer.BYTES;
    }

    public void putByte(byte value) {
        ensureWritable(Byte.BYTES);
        buffer.put(position, value);
        position += Byte.BYTES;
    }

    public void putShort(short value) {
        ensureWritable(Short.BYTES);
        buffer.putShort(position, value);
        position += Short.BYTES;
    }

    public void putPackedUV(float u, float v) {
        int encodedU = Math.round(clamp(u, 0.0f, 1.0f) * 65535.0f);
        int encodedV = Math.round(clamp(v, 0.0f, 1.0f) * 65535.0f);
        putShort((short) encodedU);
        putShort((short) encodedV);
    }

    public void putPackedPos(float x, float y, float z) {
        ensureWritable(3);
        buffer.put(position, toSignedByte(x));
        buffer.put(position + 1, toSignedByte(y));
        buffer.put(position + 2, toSignedByte(z));
        position += 3;
    }

    public void reset() {
        position = 0;
    }

    public ByteBuffer getByteBuffer() {
        return buffer.duplicate().order(ByteOrder.nativeOrder());
    }

    public ByteBuffer readableSlice() {
        ByteBuffer duplicate = getByteBuffer();
        duplicate.position(0);
        duplicate.limit(position);
        return duplicate.slice().order(ByteOrder.nativeOrder());
    }

    public int getPosition() {
        return position;
    }

    public int getCapacity() {
        return buffer.capacity();
    }

    public boolean isEmpty() {
        return position == 0;
    }

    private void ensureWritable(int bytes) {
        if (position + bytes <= buffer.capacity()) {
            return;
        }

        int newCapacity = buffer.capacity();
        while (position + bytes > newCapacity) {
            newCapacity = Math.max(newCapacity * 2, position + bytes);
        }

        ByteBuffer replacement = MemoryManager.allocate(newCapacity);
        ByteBuffer source = readableSlice();
        replacement.put(source);
        MemoryManager.free(buffer);
        buffer = replacement;
    }

    private static float clamp(float value, float min, float max) {
        return Math.max(min, Math.min(max, value));
    }

    private static byte toSignedByte(float value) {
        int scaled = Math.round(clamp(value, -1.0f, 1.0f) * 127.0f);
        return (byte) scaled;
    }
}
