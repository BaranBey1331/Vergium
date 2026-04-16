package com.vergium.mixin;

import com.vergium.core.render.VisibilityManager;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * FIXED: Using absolute target string with remap=false for inner class stability.
 */
@Mixin(targets = "net.minecraft.client.renderer.chunk.SectionRenderDispatcher$RenderSection", remap = false)
public class MixinRenderSection {

    /**
     * Bypasses vanilla chunk compilation and uses Vergium Engine instead.
     */
    @Inject(method = "getBB", at = @At("RETURN"), cancellable = true)
    private void onGetBB(CallbackInfoReturnable<AABB> cir) {
        AABB aabb = cir.getReturnValue();
        if (aabb != null && !VisibilityManager.isVisibleInFrustum(aabb)) {
            cir.setReturnValue(new AABB(0, 0, 0, 0, 0, 0));
        }
    }
}
