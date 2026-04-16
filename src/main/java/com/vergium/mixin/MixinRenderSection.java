package com.vergium.mixin;

import com.vergium.core.render.VisibilityManager;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * FIXED: Use safe target string for inner class to avoid resolution issues during compilation.
 */
@Mixin(targets = "net.minecraft.client.renderer.chunk.SectionRenderDispatcher$RenderSection")
public class MixinRenderSection {

    /**
     * Truly cancels rendering by returning an empty AABB for non-visible sections.
     */
    @Inject(method = "getBB", at = @At("RETURN"), cancellable = true, remap = false)
    private void onGetBB(CallbackInfoReturnable<AABB> cir) {
        AABB aabb = cir.getReturnValue();
        if (aabb != null && !VisibilityManager.isVisibleInFrustum(aabb)) {
            cir.setReturnValue(new AABB(0, 0, 0, 0, 0, 0));
        }
    }
}
