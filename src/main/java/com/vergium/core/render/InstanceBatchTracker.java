package com.vergium.core.render;

import com.vergium.core.memory.NativeBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Pure-Java instance planner separated from GL submission.
 */
public final class InstanceBatchTracker {
    public record InstancePlan(String entityType, int meshVbo, int vertexCount, int instanceCount, NativeBuffer instanceData) {
    }

    private final Map<String, Integer> instanceCounts = new ConcurrentHashMap<>();
    private final Map<String, NativeBuffer> instanceBuffers = new ConcurrentHashMap<>();
    private final Map<String, Integer> meshVbos = new ConcurrentHashMap<>();
    private final Map<String, Integer> meshVertexCounts = new ConcurrentHashMap<>();

    public void registerMesh(String entityType, int vbo, int vertexCount) {
        if (entityType == null || entityType.isBlank() || vbo <= 0 || vertexCount <= 0) {
            return;
        }

        meshVbos.put(entityType, vbo);
        meshVertexCounts.put(entityType, vertexCount);
    }

    public void submit(String entityType, float x, float y, float z) {
        if (entityType == null || entityType.isBlank()) {
            return;
        }

        instanceCounts.merge(entityType, 1, Integer::sum);
        NativeBuffer buffer = instanceBuffers.computeIfAbsent(entityType, key -> new NativeBuffer(16 * 1024));
        buffer.putFloat(x);
        buffer.putFloat(y);
        buffer.putFloat(z);
    }

    public List<InstancePlan> snapshotPlans() {
        List<InstancePlan> plans = new ArrayList<>();
        instanceCounts.forEach((type, count) -> {
            if (count <= 0) {
                return;
            }

            Integer meshVbo = meshVbos.get(type);
            Integer vertexCount = meshVertexCounts.get(type);
            NativeBuffer instanceData = instanceBuffers.get(type);
            if (meshVbo == null || vertexCount == null || instanceData == null || instanceData.isEmpty()) {
                return;
            }

            plans.add(new InstancePlan(type, meshVbo, vertexCount, count, instanceData));
        });
        return plans;
    }

    public int getPendingInstanceCount() {
        return instanceCounts.values().stream().mapToInt(Integer::intValue).sum();
    }

    public Set<String> getTrackedEntityTypes() {
        return Set.copyOf(instanceBuffers.keySet());
    }

    public void resetCounts() {
        instanceCounts.clear();
        instanceBuffers.values().forEach(NativeBuffer::reset);
    }

    public void clearAll() {
        instanceCounts.clear();
        instanceBuffers.clear();
        meshVbos.clear();
        meshVertexCounts.clear();
    }
}
