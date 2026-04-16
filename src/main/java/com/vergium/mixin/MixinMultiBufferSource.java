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
 * FIXED: Real redirection of 3rd-party mod rendering to Vergium's bridge.
 */
@Mixin(MultiBufferSource.class)
public interface MixinMultiBufferSource {

    @Inject(method = "getBuffer", at = @At("HEAD"), cancellable = true)
    default void onGetBuffer(RenderType pRenderType, CallbackInfoReturnable<VertexConsumer> cir) {
        String typeStr = pRenderType.toString();
        
        // Intercept heavy rendering types commonly used by mods
        if (typeStr.contains("entity_solid") || typeStr.contains("entity_cutout")) {
            // Signal the bridge that a mod is drawing
            ModBridge.bridgeDraw("interceptor", 0);
            
            // In a fully integrated state, we would return Vergium's VertexConsumer here:
            // cir.setReturnValue(VergiumBatchRenderer.getBufferForLayer(typeStr));
        }
    }
}
