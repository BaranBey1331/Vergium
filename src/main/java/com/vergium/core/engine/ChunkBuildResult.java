package com.vergium.core.engine;

/**
 * Deterministic output of the chunk build planner used by tests and fallback simulation.
 */
public record ChunkBuildResult(
        int chunkX,
        int chunkY,
        int chunkZ,
        int visibleFaces,
        int vertexCount,
        String targetLayer,
        boolean fallbackStaged
) {
}
