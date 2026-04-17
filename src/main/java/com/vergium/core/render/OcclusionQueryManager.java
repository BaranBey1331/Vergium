package com.vergium.core.render;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL33;

/**
 * Query-id recycler with no-context fallbacks.
 */
public final class OcclusionQueryManager {
    private static final int MAX_POOLED_QUERIES = 1024;

    private static final Queue<Integer> QUERY_POOL = new ConcurrentLinkedQueue<>();
    private static final AtomicInteger QUERY_COUNT = new AtomicInteger();

    private OcclusionQueryManager() {
    }

    public static int acquireQuery() {
        if (!isGlContextAvailable()) {
            return 0;
        }

        Integer pooled = QUERY_POOL.poll();
        if (pooled != null) {
            QUERY_COUNT.decrementAndGet();
            return pooled;
        }
        return GL15.glGenQueries();
    }

    public static void releaseQuery(int queryId) {
        if (queryId <= 0) {
            return;
        }

        while (true) {
            int current = QUERY_COUNT.get();
            if (current >= MAX_POOLED_QUERIES) {
                return;
            }
            if (QUERY_COUNT.compareAndSet(current, current + 1)) {
                QUERY_POOL.offer(queryId);
                return;
            }
        }
    }

    public static void beginQuery(int queryId) {
        if (queryId > 0 && isGlContextAvailable()) {
            GL15.glBeginQuery(GL33.GL_ANY_SAMPLES_PASSED, queryId);
        }
    }

    public static void endQuery() {
        if (isGlContextAvailable()) {
            GL15.glEndQuery(GL33.GL_ANY_SAMPLES_PASSED);
        }
    }

    public static boolean isResultAvailable(int queryId) {
        if (queryId <= 0 || !isGlContextAvailable()) {
            return false;
        }
        return GL15.glGetQueryObjecti(queryId, GL15.GL_QUERY_RESULT_AVAILABLE) != 0;
    }

    public static int getQueryResult(int queryId) {
        if (queryId <= 0 || !isGlContextAvailable()) {
            return 0;
        }
        return GL15.glGetQueryObjecti(queryId, GL15.GL_QUERY_RESULT);
    }

    public static int getPooledQueryCount() {
        return QUERY_COUNT.get();
    }

    static void clear() {
        QUERY_POOL.clear();
        QUERY_COUNT.set(0);
    }

    private static boolean isGlContextAvailable() {
        try {
            return GL.getCapabilities() != null;
        } catch (IllegalStateException ex) {
            return false;
        }
    }
}
