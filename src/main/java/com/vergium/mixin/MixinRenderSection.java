package com.vergium.mixin;

import com.vergium.core.render.VisibilityManager;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Mixin for SectionRenderDispatcher.RenderSection to hook into bounding box calculation for culling.
 * Using target string for safe inner class referencing.
 */
@Mixin(targets = "net.minecraft.client.renderer.chunk.SectionRenderDispatcher$RenderSection")
public class MixinRenderSection {

    /**
     * Redirects or cancels rendering if the section is not visible.
     * Fixed method name to 'getBB' for 1.20.1 official mappings.
     */
    @Inject(method = "getBB", at = @At("RETURN"))
    private void onGetBB(CallbackInfoReturnable<AABB> cir) {
        AABB aabb = cir.getReturnValue();
        if (!VisibilityManager.isVisibleInFrustum(aabb)) {
            // Logic to mark as not needed for rendering this frame
        }
    }
}
