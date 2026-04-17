package com.vergium.mixin;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.vergium.core.bridge.ModBridge;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MultiBufferSource.BufferSource.class)
public abstract class MixinMultiBufferSource {

    @Inject(method = "getBuffer", at = @At("HEAD"))
    private void onGetBuffer(RenderType renderType, CallbackInfoReturnable<VertexConsumer> cir) {
        if (renderType == null) {
            return;
        }

        String typeName = renderType.toString();
        if (typeName.contains("entity_solid") || typeName.contains("entity_cutout")) {
            ModBridge.bridgeDraw("interceptor", 0);
        }
    }
}
