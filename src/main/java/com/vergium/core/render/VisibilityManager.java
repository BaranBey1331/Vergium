package com.vergium.core.render;

import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.world.phys.AABB;

/**
 * Stores the current camera frustum and combines it with query feedback.
 */
public final class VisibilityManager {
    private static volatile Frustum currentFrustum;

    private VisibilityManager() {
    }

    public static void updateFrustum(Frustum frustum) {
        currentFrustum = frustum;
    }

    public static void clearFrustum() {
        currentFrustum = null;
    }

    public static boolean isVisibleInFrustum(AABB aabb) {
        if (aabb == null) {
            return false;
        }

        Frustum frustum = currentFrustum;
        if (frustum == null) {
            return true;
        }
        return frustum.isVisible(aabb);
    }

    public static boolean shouldRender(AABB aabb, int lastQueryId) {
        if (!isVisibleInFrustum(aabb)) {
            return false;
        }

        if (lastQueryId > 0 && OcclusionQueryManager.isResultAvailable(lastQueryId)) {
            return OcclusionQueryManager.getQueryResult(lastQueryId) > 0;
        }
        return true;
    }
}
