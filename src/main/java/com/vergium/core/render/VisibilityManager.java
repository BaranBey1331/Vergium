package com.vergium.core.render;

import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.world.phys.AABB;

/**
 * Handles hierarchical visibility tests to skip unnecessary rendering work.
 */
public class VisibilityManager {
    private static Frustum currentFrustum;

    /**
     * Updates the current camera frustum for visibility tests.
     */
    public static void updateFrustum(Frustum frustum) {
        currentFrustum = frustum;
    }

    /**
     * Fast CPU-side test to check if a chunk's bounding box is inside the camera frustum.
     */
    public static boolean isVisibleInFrustum(AABB aabb) {
        if (currentFrustum == null) return true;
        return currentFrustum.isVisible(aabb);
    }

    /**
     * "Smart Culling": Combines frustum tests and potentially previous frame occlusion data
     * to decide if a chunk should be rendered.
     */
    public static boolean shouldRender(AABB aabb, int lastQueryId) {
        // Step 1: Frustum Culling (Fastest)
        if (!isVisibleInFrustum(aabb)) {
            return false;
        }

        // Step 2: Check Occlusion Query Result from last frame if available
        if (lastQueryId != -1 && OcclusionQueryManager.isResultAvailable(lastQueryId)) {
            return OcclusionQueryManager.getQueryResult(lastQueryId) > 0;
        }

        // Default to visible if no occlusion data is ready yet
        return true;
    }
}
