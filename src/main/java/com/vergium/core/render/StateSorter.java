package com.vergium.core.render;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Sorts draw calls into a stable, low-state-churn order.
 */
public final class StateSorter {
    private final List<DrawCall> drawCalls = new ArrayList<>();

    public static final class DrawCall {
        private final int shaderId;
        private final int textureId;
        private final int bufferOffset;
        private final int vertexCount;

        public DrawCall(int shaderId, int textureId, int bufferOffset, int vertexCount) {
            this.shaderId = shaderId;
            this.textureId = textureId;
            this.bufferOffset = bufferOffset;
            this.vertexCount = vertexCount;
        }

        public int shaderId() {
            return shaderId;
        }

        public int textureId() {
            return textureId;
        }

        public int bufferOffset() {
            return bufferOffset;
        }

        public int vertexCount() {
            return vertexCount;
        }
    }

    public void add(int shaderId, int textureId, int bufferOffset, int vertexCount) {
        drawCalls.add(new DrawCall(shaderId, textureId, bufferOffset, vertexCount));
    }

    public List<DrawCall> drainSorted() {
        drawCalls.sort(Comparator.comparingInt(DrawCall::shaderId)
                .thenComparingInt(DrawCall::textureId)
                .thenComparingInt(DrawCall::bufferOffset));
        List<DrawCall> sorted = new ArrayList<>(drawCalls);
        drawCalls.clear();
        return sorted;
    }

    public int size() {
        return drawCalls.size();
    }

    public void flush() {
        drainSorted();
    }
}
