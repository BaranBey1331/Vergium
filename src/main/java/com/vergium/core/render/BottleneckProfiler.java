package com.vergium.core.render;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * FIXED: Thread-safe bottleneck detection using Atomic variables.
 */
public class BottleneckProfiler {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final AtomicLong lastFrameTime = new AtomicLong(0);
    private static final AtomicInteger frameCount = new AtomicInteger(0);
    private static float cpuWaitTime = 0.0f; 

    public static void startFrame() {
        lastFrameTime.set(System.nanoTime());
    }

    public static void endFrame() {
        long duration = System.nanoTime() - lastFrameTime.get();
        int count = frameCount.incrementAndGet();

        if (count % 600 == 0) {
            float fps = 1_000_000_000.0f / duration;
            analyze(fps);
        }
    }

    private static synchronized void analyze(float fps) {
        if (fps < 30.0f) {
            LOGGER.warn("Bottleneck Detected! FPS: " + fps);
        }
    }
}
