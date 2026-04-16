package com.vergium.mixin;

import com.vergium.core.render.VergiumBatchRenderer;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Intercepts MultiBufferSource.getBuffer() to optimize 3rd-party mods.
 * Redirects them into Vergium's optimized rendering paths.
 */
@Mixin(MultiBufferSource.class)
public interface MixinMultiBufferSource {

    @Inject(method = "getBuffer", at = @At("HEAD"), cancellable = true)
    default void onGetBuffer(RenderType pRenderType, CallbackInfoReturnable<VertexConsumer> cir) {
        // If the mod is requesting a standard render type, we can potentially
        // redirect it to our batched buffer system.
        if (pRenderType.toString().contains("entity_solid")) {
            // cir.setReturnValue(VergiumBatchRenderer.getBufferForLayer("entity_solid"));
        }
    }
}
