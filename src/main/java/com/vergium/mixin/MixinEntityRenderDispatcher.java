package com.vergium.mixin;

import com.vergium.core.render.VisibilityManager;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderDispatcher.class)
public class MixinEntityRenderDispatcher {

    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    private void onRenderEntity(
            Entity entity,
            double x,
            double y,
            double z,
            float yaw,
            float partialTicks,
            com.mojang.blaze3d.vertex.PoseStack poseStack,
            net.minecraft.client.renderer.MultiBufferSource bufferSource,
            int light,
            CallbackInfo ci
    ) {
        if (entity == null) {
            return;
        }

        AABB aabb = entity.getBoundingBoxForCulling();
        if (aabb != null && aabb.getSize() > 0.0D && !VisibilityManager.isVisibleInFrustum(aabb)) {
            ci.cancel();
        }
    }
}
