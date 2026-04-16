package com.vergium.core.render;

import com.mojang.blaze3d.systems.RenderSystem;
import java.util.LinkedList;
import java.util.Queue;
import org.lwjgl.opengl.GLES30;

/**
 * Manages OpenGL ES 3.2 Occlusion Queries.
 * Used to determine if a chunk is hidden by other geometry.
 */
public class OcclusionQueryManager {
    private static final Queue<Integer> QUERY_POOL = new LinkedList<>();
    private static final int INITIAL_POOL_SIZE = 128;

    static {
        // Note: OpenGL commands must run on the render thread.
        // This static block is for conceptual initialization.
    }

    /**
     * Acquires a query ID from the pool or generates a new one.
     */
    public static int acquireQuery() {
        if (QUERY_POOL.isEmpty()) {
            return GLES30.glGenQueries();
        }
        return QUERY_POOL.poll();
    }

    /**
     * Returns a query ID to the pool for reuse.
     */
    public static void releaseQuery(int queryId) {
        QUERY_POOL.add(queryId);
    }

    /**
     * Starts an occlusion query.
     */
    public static void beginQuery(int queryId) {
        GLES30.glBeginQuery(GLES30.GL_ANY_SAMPLES_PASSED, queryId);
    }

    /**
     * Ends the current occlusion query.
     */
    public static void endQuery() {
        GLES30.glEndQuery(GLES30.GL_ANY_SAMPLES_PASSED);
    }

    /**
     * Checks if the results of a query are available.
     */
    public static boolean isResultAvailable(int queryId) {
        int[] available = new int[1];
        GLES30.glGetQueryObjectuiv(queryId, GLES30.GL_QUERY_RESULT_AVAILABLE, available, 0);
        return available[0] != 0;
    }

    /**
     * Gets the result of an occlusion query (number of samples passed).
     */
    public static int getQueryResult(int queryId) {
        int[] result = new int[1];
        GLES30.glGetQueryObjectuiv(queryId, GLES30.GL_QUERY_RESULT, result, 0);
        return result[0];
    }
}
