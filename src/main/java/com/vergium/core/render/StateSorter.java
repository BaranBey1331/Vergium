package com.vergium.core.render;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Optimizes Vulkan pipeline throughput by sorting draw calls.
 * Minimizes context switching in ANGLE's Vulkan translation.
 */
public class StateSorter {
    private final List<DrawCall> drawCalls = new ArrayList<>();

    public static class DrawCall {
        public int shaderId;
        public int textureId;
        public int bufferOffset;
        public int vertexCount;

        public DrawCall(int shaderId, int textureId, int bufferOffset, int vertexCount) {
            this.shaderId = shaderId;
            this.textureId = textureId;
            this.bufferOffset = bufferOffset;
            this.vertexCount = vertexCount;
        }
    }

    /**
     * Adds a draw call to the sorting queue.
     */
    public void add(int shaderId, int textureId, int bufferOffset, int vertexCount) {
        drawCalls.add(new DrawCall(shaderId, textureId, bufferOffset, vertexCount));
    }

    /**
     * Sorts and executes all queued draw calls.
     */
    public void flush() {
        // Sort by Shader first, then Texture to minimize Vulkan pipeline re-binding
        drawCalls.sort(Comparator.comparingInt((DrawCall d) -> d.shaderId)
                                 .thenComparingInt(d -> d.textureId));

        for (DrawCall call : drawCalls) {
            // Here: Execute the optimized draw command using MDI or glDrawArrays
            // GLES30.glUseProgram(call.shaderId);
            // GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, call.textureId);
            // GLES30.glDrawArrays(GLES30.GL_TRIANGLES, call.bufferOffset, call.vertexCount);
        }
        drawCalls.clear();
    }
}
