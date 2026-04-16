package com.vergium.core.engine;

import com.vergium.core.memory.NativeBuffer;

/**
 * High-performance mesh builder that writes directly to off-heap memory.
 * Bypasses vanilla Minecraft VertexConsumer completely.
 */
public class VergiumMeshBuilder {
    private final NativeBuffer buffer;

    public VergiumMeshBuilder(int initialSize) {
        this.buffer = new NativeBuffer(initialSize);
    }

    /**
     * Adds a vertex to the mesh with packed UVs.
     */
    public void vertex(float x, float y, float z, float u, float v) {
        buffer.putFloat(x);
        buffer.putFloat(y);
        buffer.putFloat(z);
        buffer.putPackedUV(u, v);
    }

    public NativeBuffer getBuffer() {
        return buffer;
    }

    public void reset() {
        buffer.reset();
    }
}
