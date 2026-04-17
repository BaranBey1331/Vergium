package com.vergium.core.render;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

class ResourceSubsystemTest {

    @AfterEach
    void tearDown() {
        ResourceManager.clearTrackedStateForTests();
        ShaderManager.clear();
        UniformBufferManager.resetForTests();
        OcclusionQueryManager.clear();
    }

    @Test
    void resourceTrackingCountsAllResourceKinds() {
        ResourceManager.trackBuffer(1);
        ResourceManager.trackShader(2);
        ResourceManager.trackVAO(3);

        assertEquals(3, ResourceManager.getTrackedResourceCount());
    }

    @Test
    void shaderManagerClearDropsLoadedState() {
        ShaderManager.clear();

        assertEquals(0, ShaderManager.getLoadedShaderCount());
    }

    @Test
    void occlusionManagerClearResetsPoolCounters() {
        OcclusionQueryManager.releaseQuery(7);
        assertEquals(1, OcclusionQueryManager.getPooledQueryCount());

        OcclusionQueryManager.clear();

        assertEquals(0, OcclusionQueryManager.getPooledQueryCount());
    }
}
