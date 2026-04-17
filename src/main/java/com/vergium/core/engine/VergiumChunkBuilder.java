package com.vergium.core.engine;

/**
 * Deterministic chunk build planner used by the experimental renderer.
 */
public final class VergiumChunkBuilder {
    private static final int INITIAL_MESH_CAPACITY = 8 * 1024;

    private VergiumChunkBuilder() {
    }

    public static ChunkBuildResult build(int chunkX, int chunkY, int chunkZ) {
        VergiumMeshBuilder builder = new VergiumMeshBuilder(INITIAL_MESH_CAPACITY);
        int visibleFaces = calculateVisibleFaces(chunkX, chunkY, chunkZ);

        for (int face = 0; face < visibleFaces; face++) {
            appendFace(builder, chunkX, chunkY, chunkZ, face);
        }

        VergiumRenderDispatcher.DispatchResult dispatchResult = VergiumRenderDispatcher.dispatch(builder);
        return new ChunkBuildResult(
                chunkX,
                chunkY,
                chunkZ,
                visibleFaces,
                visibleFaces * 6,
                "chunk_layer",
                dispatchResult.mode() == VergiumRenderDispatcher.DispatchMode.FALLBACK_STAGED
        );
    }

    static int calculateVisibleFaces(int chunkX, int chunkY, int chunkZ) {
        int hash = Math.abs((chunkX * 7349) ^ (chunkY * 9151) ^ (chunkZ * 1531));
        return 1 + (hash % 6);
    }

    private static void appendFace(VergiumMeshBuilder builder, int chunkX, int chunkY, int chunkZ, int faceIndex) {
        float baseX = (chunkX & 15) + (faceIndex % 2);
        float baseY = (chunkY & 15) + ((faceIndex / 2) % 2);
        float baseZ = (chunkZ & 15) + ((faceIndex / 4) % 2);

        builder.vertex(baseX, baseY, baseZ, 0.0f, 0.0f);
        builder.vertex(baseX + 1.0f, baseY, baseZ, 1.0f, 0.0f);
        builder.vertex(baseX + 1.0f, baseY + 1.0f, baseZ, 1.0f, 1.0f);
        builder.vertex(baseX, baseY, baseZ, 0.0f, 0.0f);
        builder.vertex(baseX + 1.0f, baseY + 1.0f, baseZ, 1.0f, 1.0f);
        builder.vertex(baseX, baseY + 1.0f, baseZ, 0.0f, 1.0f);
    }
}
