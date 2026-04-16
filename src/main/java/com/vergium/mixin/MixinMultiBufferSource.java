package com.vergium.mixin;

import com.vergium.core.bridge.ModBridge;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * FIXED: Target the MultiBufferSource.BufferSource class explicitly to resolve interface mismatch.
 */
@Mixin(targets = "net.minecraft.client.renderer.MultiBufferSource$BufferSource")
public abstract class MixinMultiBufferSource {

    @Inject(method = "getBuffer", at = @At("HEAD"), cancellable = true)
    private void onGetBuffer(RenderType pRenderType, CallbackInfoReturnable<VertexConsumer> cir) {
        String typeStr = pRenderType.toString();
        if (typeStr.contains("entity_solid") || typeStr.contains("entity_cutout")) {
            ModBridge.bridgeDraw("interceptor", 0);
        }
    }
}
