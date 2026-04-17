package com.vergium.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.vergium.core.render.BottleneckProfiler;
import com.vergium.core.render.VergiumBatchRenderer;
import com.vergium.core.render.VisibilityManager;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.culling.Frustum;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public class MixinLevelRenderer {

    @Inject(method = "renderLevel", at = @At("HEAD"))
    private void onRenderLevelStart(
            PoseStack poseStack,
            float partialTick,
            long finishNanoTime,
            boolean renderBlockOutline,
            Camera camera,
            GameRenderer gameRenderer,
            LightTexture lightTexture,
            Matrix4f projectionMatrix,
            CallbackInfo ci
    ) {
        BottleneckProfiler.startFrame();
    }

    @Inject(method = "setupRender", at = @At("RETURN"))
    private void onSetupRenderEnd(Camera camera, Frustum frustum, boolean hasCapturedFrustum, boolean isSpectator, CallbackInfo ci) {
        VisibilityManager.updateFrustum(frustum);
    }

    @Inject(method = "renderLevel", at = @At("RETURN"))
    private void onRenderLevelEnd(
            PoseStack poseStack,
            float partialTick,
            long finishNanoTime,
            boolean renderBlockOutline,
            Camera camera,
            GameRenderer gameRenderer,
            LightTexture lightTexture,
            Matrix4f projectionMatrix,
            CallbackInfo ci
    ) {
        VergiumBatchRenderer.flush();
        BottleneckProfiler.endFrame();
    }
}
