package com.vergium.core.engine;

import com.vergium.core.pipeline.DispatchPipeline;
import com.vergium.core.pipeline.VulkanFastPath;
import com.vergium.core.render.CommandBuffer;
import com.vergium.core.render.VergiumBatchRenderer;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Dispatches custom meshes to the fast path when a GL context is available.
 */
public final class VergiumRenderDispatcher {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final DispatchPipeline DEFAULT_PIPELINE = new LazyDispatchPipeline();
    private static final AtomicInteger DISPATCH_COUNT = new AtomicInteger();
    private static final AtomicInteger FALLBACK_COUNT = new AtomicInteger();

    private static volatile DispatchPipeline pipeline = DEFAULT_PIPELINE;

    private VergiumRenderDispatcher() {
    }

    public static DispatchResult dispatch(VergiumMeshBuilder mesh) {
        if (mesh == null || mesh.isEmpty()) {
            return new DispatchResult(DispatchMode.SKIPPED_EMPTY, 0, 0);
        }

        DISPATCH_COUNT.incrementAndGet();

        if (!pipeline.isAvailable()) {
            VergiumBatchRenderer.stageMesh("custom_engine_layer", mesh);
            int vertexCount = mesh.getVertexCount();
            mesh.reset();
            FALLBACK_COUNT.incrementAndGet();
            return new DispatchResult(DispatchMode.FALLBACK_STAGED, vertexCount, 1);
        }

        CommandBuffer commands = new CommandBuffer(1);
        commands.addCommand(mesh.getVertexCount(), 1, 0, 0);
        pipeline.setCommandBuffer(commands);

        try {
            pipeline.prepare();
            pipeline.render(0, mesh.getVertexCount());
            pipeline.finish();
            return new DispatchResult(DispatchMode.DIRECT_GPU, mesh.getVertexCount(), commands.getCommandCount());
        } catch (RuntimeException ex) {
            LOGGER.debug("Vergium fast-path dispatch skipped because the GL context was not ready.", ex);
            VergiumBatchRenderer.stageMesh("custom_engine_layer", mesh);
            FALLBACK_COUNT.incrementAndGet();
            return new DispatchResult(DispatchMode.FALLBACK_STAGED, mesh.getVertexCount(), commands.getCommandCount());
        } finally {
            mesh.reset();
        }
    }

    static void setPipelineForTests(DispatchPipeline testPipeline) {
        pipeline = testPipeline == null ? DEFAULT_PIPELINE : testPipeline;
    }

    static void resetForTests() {
        pipeline = DEFAULT_PIPELINE;
        DISPATCH_COUNT.set(0);
        FALLBACK_COUNT.set(0);
    }

    public static int getDispatchCount() {
        return DISPATCH_COUNT.get();
    }

    public static int getFallbackCount() {
        return FALLBACK_COUNT.get();
    }

    public enum DispatchMode {
        SKIPPED_EMPTY,
        DIRECT_GPU,
        FALLBACK_STAGED
    }

    public record DispatchResult(DispatchMode mode, int vertexCount, int commandCount) {
    }

    private static final class LazyDispatchPipeline implements DispatchPipeline {
        private volatile DispatchPipeline delegate;

        @Override
        public boolean isAvailable() {
            return resolve().isAvailable();
        }

        @Override
        public void setCommandBuffer(CommandBuffer commands) {
            resolve().setCommandBuffer(commands);
        }

        @Override
        public void prepare() {
            resolve().prepare();
        }

        @Override
        public void render(int bufferId, int vertexCount) {
            resolve().render(bufferId, vertexCount);
        }

        @Override
        public void finish() {
            resolve().finish();
        }

        private DispatchPipeline resolve() {
            DispatchPipeline current = delegate;
            if (current != null) {
                return current;
            }

            synchronized (this) {
                if (delegate == null) {
                    try {
                        delegate = new VulkanFastPath();
                    } catch (Throwable throwable) {
                        LOGGER.debug("Falling back to unavailable dispatch pipeline because VulkanFastPath could not initialize.", throwable);
                        delegate = new UnavailableDispatchPipeline();
                    }
                }
                return delegate;
            }
        }
    }

    private static final class UnavailableDispatchPipeline implements DispatchPipeline {
        @Override
        public boolean isAvailable() {
            return false;
        }

        @Override
        public void setCommandBuffer(CommandBuffer commands) {
        }

        @Override
        public void prepare() {
        }

        @Override
        public void render(int bufferId, int vertexCount) {
        }

        @Override
        public void finish() {
        }
    }
}
