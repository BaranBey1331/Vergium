package com.vergium.core.render;

import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL33;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Manages OpenGL Occlusion Queries.
 * Used to determine if a chunk is hidden by other geometry.
 * Compatible with standard LWJGL used by Minecraft.
 */
public class OcclusionQueryManager {
    private static final Queue<Integer> QUERY_POOL = new LinkedList<>();

    /**
     * Acquires a query ID from the pool or generates a new one.
     */
    public static int acquireQuery() {
        if (QUERY_POOL.isEmpty()) {
            return GL15.glGenQueries();
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
        GL15.glBeginQuery(GL33.GL_ANY_SAMPLES_PASSED, queryId);
    }

    /**
     * Ends the current occlusion query.
     */
    public static void endQuery() {
        GL15.glEndQuery(GL33.GL_ANY_SAMPLES_PASSED);
    }

    /**
     * Checks if the results of a query are available.
     */
    public static boolean isResultAvailable(int queryId) {
        return GL15.glGetQueryObjecti(queryId, GL15.GL_QUERY_RESULT_AVAILABLE) != 0;
    }

    /**
     * Gets the result of an occlusion query (number of samples passed).
     */
    public static int getQueryResult(int queryId) {
        return GL15.glGetQueryObjecti(queryId, GL15.GL_QUERY_RESULT);
    }
}
