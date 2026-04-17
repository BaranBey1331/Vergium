package com.vergium.core.engine;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.vergium.core.pipeline.DispatchPipeline;
import com.vergium.core.render.CommandBuffer;
import com.vergium.core.render.VergiumBatchRenderer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

class VergiumRenderDispatcherTest {

    @AfterEach
    void tearDown() {
        VergiumRenderDispatcher.resetForTests();
        VergiumBatchRenderer.clearAll();
    }

    @Test
    void directPipelinePathUsesCommandBuffer() {
        FakePipeline pipeline = new FakePipeline(true, false);
        VergiumRenderDispatcher.setPipelineForTests(pipeline);
        VergiumMeshBuilder builder = new VergiumMeshBuilder(16);
        builder.vertex(0, 0, 0, 0, 0);

        VergiumRenderDispatcher.DispatchResult result = VergiumRenderDispatcher.dispatch(builder);

        assertEquals(VergiumRenderDispatcher.DispatchMode.DIRECT_GPU, result.mode());
        assertEquals(1, pipeline.prepareCount);
        assertEquals(1, pipeline.lastCommandCount);
    }

    @Test
    void failingPipelineFallsBackToStaging() {
        FakePipeline pipeline = new FakePipeline(true, true);
        VergiumRenderDispatcher.setPipelineForTests(pipeline);
        VergiumMeshBuilder builder = new VergiumMeshBuilder(16);
        builder.vertex(0, 0, 0, 0, 0);

        VergiumRenderDispatcher.DispatchResult result = VergiumRenderDispatcher.dispatch(builder);

        assertEquals(VergiumRenderDispatcher.DispatchMode.FALLBACK_STAGED, result.mode());
        assertEquals(1, VergiumRenderDispatcher.getFallbackCount());
        assertEquals(1, VergiumBatchRenderer.getStagedVertexCount());
    }

    private static final class FakePipeline implements DispatchPipeline {
        private final boolean available;
        private final boolean failOnRender;
        private int lastCommandCount;
        private int prepareCount;

        private FakePipeline(boolean available, boolean failOnRender) {
            this.available = available;
            this.failOnRender = failOnRender;
        }

        @Override
        public boolean isAvailable() {
            return available;
        }

        @Override
        public void setCommandBuffer(CommandBuffer commands) {
            this.lastCommandCount = commands.getCommandCount();
        }

        @Override
        public void prepare() {
            prepareCount++;
        }

        @Override
        public void render(int bufferId, int vertexCount) {
            if (failOnRender) {
                throw new RuntimeException("boom");
            }
        }

        @Override
        public void finish() {
        }
    }
}
