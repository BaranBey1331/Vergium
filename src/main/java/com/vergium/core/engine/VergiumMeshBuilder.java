package com.vergium.core.engine;

import com.vergium.core.memory.NativeBuffer;

/**
 * Writes compact vertex data directly into a growable native buffer.
 */
public final class VergiumMeshBuilder {
    private final NativeBuffer buffer;
    private int vertexCount;

    public VergiumMeshBuilder(int initialSize) {
        this.buffer = new NativeBuffer(initialSize);
    }

    public void vertex(float x, float y, float z, float u, float v) {
        buffer.putFloat(x);
        buffer.putFloat(y);
        buffer.putFloat(z);
        buffer.putPackedUV(u, v);
        vertexCount++;
    }

    public NativeBuffer getBuffer() {
        return buffer;
    }

    public int getVertexCount() {
        return vertexCount;
    }

    public boolean isEmpty() {
        return vertexCount == 0;
    }

    public void reset() {
        vertexCount = 0;
        buffer.reset();
    }
}
