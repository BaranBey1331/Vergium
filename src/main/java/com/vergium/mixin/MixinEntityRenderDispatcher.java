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

    /**
     * Prevents rendering of entities that are not visible to the camera.
     * Reduces draw calls and CPU overhead on mobile launchers.
     */
    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    private void onRenderEntity(Entity entity, double x, double y, double z, float yaw, float partialTicks, com.mojang.blaze3d.vertex.PoseStack poseStack, net.minecraft.client.renderer.MultiBufferSource bufferSource, int light, CallbackInfo ci) {
        AABB aabb = entity.getBoundingBoxForCulling();
        if (!VisibilityManager.isVisibleInFrustum(aabb)) {
            ci.cancel(); // "Gereksiz" entity çizimini durdur.
        }
    }
}
