package com.vergium.mixin;

import com.vergium.core.render.VisibilityManager;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "net.minecraft.client.renderer.chunk.SectionRenderDispatcher$RenderSection")
public class MixinRenderSection {

    /**
     * FIXED: Truly cancels rendering and returns a dummy AABB if not visible.
     */
    @Inject(method = "getBB", at = @At("RETURN"), cancellable = true)
    private void onGetBB(CallbackInfoReturnable<AABB> cir) {
        AABB aabb = cir.getReturnValue();
        if (!VisibilityManager.isVisibleInFrustum(aabb)) {
            // Cancel and return a minimal AABB to stop further processing in vanilla code
            cir.setReturnValue(new AABB(0, 0, 0, 0, 0, 0));
        }
    }
}
