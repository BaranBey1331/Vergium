package com.vergium;

import com.vergium.core.memory.BufferPool;
import com.vergium.core.render.ResourceManager;
import com.vergium.core.render.VergiumBatchRenderer;
import com.vergium.core.render.VisibilityManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(Vergium.MOD_ID)
public final class Vergium {
    public static final String MOD_ID = "vergium";

    private static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public Vergium() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setupClient);
        MinecraftForge.EVENT_BUS.addListener(this::onLevelUnload);
        LOGGER.info("Vergium initialized with hardened lifecycle guards.");
    }

    private void setupClient(final FMLClientSetupEvent event) {
        LOGGER.debug("Vergium client setup event received.");
    }

    private void onLevelUnload(LevelEvent.Unload event) {
        LOGGER.info("World unload detected. Resetting Vergium state.");
        VergiumBatchRenderer.clearAll();
        VisibilityManager.clearFrustum();
        BufferPool.clear();
        ResourceManager.cleanup();
    }
}
