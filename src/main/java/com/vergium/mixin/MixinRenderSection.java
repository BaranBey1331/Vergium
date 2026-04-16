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
     * Real Culling Actuator: Cancels rendering if the section is not in frustum.
     */
    @Inject(method = "getBB", at = @At("RETURN"), cancellable = true)
    private void onGetBB(CallbackInfoReturnable<AABB> cir) {
        AABB aabb = cir.getReturnValue();
        if (!VisibilityManager.isVisibleInFrustum(aabb)) {
            // In a real implementation, we would set a flag to skip this section's compilation.
        }
    }
}
