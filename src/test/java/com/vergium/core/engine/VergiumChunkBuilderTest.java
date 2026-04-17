package com.vergium.core.engine;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.vergium.core.render.VergiumBatchRenderer;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class VergiumChunkBuilderTest {

    @AfterEach
    void tearDown() {
        VergiumBatchRenderer.clearAll();
        VergiumRenderDispatcher.resetForTests();
    }

    @ParameterizedTest(name = "chunk case #{index} -> ({0},{1},{2})")
    @MethodSource("chunkCoordinates")
    void buildProducesDeterministicNonEmptyResults(int x, int y, int z) {
        ChunkBuildResult result = VergiumChunkBuilder.build(x, y, z);

        int expectedFaces = VergiumChunkBuilder.calculateVisibleFaces(x, y, z);
        assertEquals(expectedFaces, result.visibleFaces());
        assertEquals(expectedFaces * 6, result.vertexCount());
        assertEquals("chunk_layer", result.targetLayer());
        assertTrue(result.visibleFaces() >= 1 && result.visibleFaces() <= 6);
    }

    @Test
    void fallbackDispatchStagesChunkGeometryWithoutGlContext() {
        ChunkBuildResult result = VergiumChunkBuilder.build(1, 2, 3);

        assertTrue(result.fallbackStaged());
        assertTrue(VergiumBatchRenderer.getTrackedLayers().contains("custom_engine_layer"));
        assertTrue(VergiumBatchRenderer.getStagedVertexCount() > 0);
    }

    static Stream<Arguments> chunkCoordinates() {
        return IntStream.range(0, 64)
                .mapToObj(index -> Arguments.of(index % 8, (index / 8) % 4, index / 4));
    }
}
