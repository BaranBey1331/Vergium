package com.vergium.core.render;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.DoubleConsumer;
import java.util.function.Consumer;
import java.util.function.LongSupplier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Lightweight frame sampler that can be tested with injected clocks/analyzers.
 */
public final class BottleneckProfiler {
    static final int SAMPLE_WINDOW = 600;

    private static final Logger LOGGER = LogManager.getLogger();
    private static final AtomicLong LAST_FRAME_TIME = new AtomicLong();
    private static final AtomicInteger FRAME_COUNT = new AtomicInteger();
    private static final AtomicLong TOTAL_FRAME_TIME = new AtomicLong();
    private static final AtomicLong SLOW_FRAME_COUNT = new AtomicLong();

    private static volatile LongSupplier timeSource = System::nanoTime;
    private static volatile Consumer<Float> analyzer = BottleneckProfiler::defaultAnalyze;
    private static volatile DoubleConsumer frameTimeConsumer = frameTimeMillis -> {};

    private BottleneckProfiler() {
    }

    public static void startFrame() {
        LAST_FRAME_TIME.set(timeSource.getAsLong());
    }

    public static void endFrame() {
        long start = LAST_FRAME_TIME.get();
        if (start == 0L) {
            return;
        }

        long duration = Math.max(1L, timeSource.getAsLong() - start);
        TOTAL_FRAME_TIME.addAndGet(duration);
        double frameTimeMillis = duration / 1_000_000.0;
        frameTimeConsumer.accept(frameTimeMillis);
        if (frameTimeMillis > 50.0) {
            SLOW_FRAME_COUNT.incrementAndGet();
        }
        int count = FRAME_COUNT.incrementAndGet();
        if (count % SAMPLE_WINDOW == 0) {
            float fps = 1_000_000_000.0f / duration;
            analyzer.accept(fps);
        }
    }

    static void setTimeSource(LongSupplier supplier) {
        timeSource = supplier;
    }

    static void setAnalyzer(Consumer<Float> consumer) {
        analyzer = consumer;
    }

    static void setFrameTimeConsumer(DoubleConsumer consumer) {
        frameTimeConsumer = consumer;
    }

    public static double getAverageFrameTimeMillis() {
        int frames = FRAME_COUNT.get();
        if (frames == 0) {
            return 0.0;
        }
        return TOTAL_FRAME_TIME.get() / 1_000_000.0 / frames;
    }

    public static long getSlowFrameCount() {
        return SLOW_FRAME_COUNT.get();
    }

    static void resetForTests() {
        FRAME_COUNT.set(0);
        LAST_FRAME_TIME.set(0L);
        TOTAL_FRAME_TIME.set(0L);
        SLOW_FRAME_COUNT.set(0L);
        timeSource = System::nanoTime;
        analyzer = BottleneckProfiler::defaultAnalyze;
        frameTimeConsumer = frameTimeMillis -> {};
    }

    private static void defaultAnalyze(float fps) {
        if (fps < 30.0f) {
            LOGGER.warn("Bottleneck detected: {} FPS", fps);
        }
    }
}
