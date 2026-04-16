package com.vergium;

import com.vergium.core.render.ResourceManager;
import com.vergium.core.render.VergiumBatchRenderer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("vergium")
public class Vergium {
    private static final Logger LOGGER = LogManager.getLogger();

    public Vergium() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setupClient);
        
        // Register world unload listener to wipe memory leaks
        MinecraftForge.EVENT_BUS.addListener(this::onLevelUnload);
        
        LOGGER.info("Vergium hardened optimization mod initialized!");
    }

    private void setupClient(final FMLClientSetupEvent event) {
        // Client-side setup logic
    }

    /**
     * Critical: Clears all static caches and off-heap memory when leaving a world.
     * Prevents OOM crashes during long sessions.
     */
    private void onLevelUnload(LevelEvent.Unload event) {
        LOGGER.info("World unload detected. Wiping Vergium caches...");
        VergiumBatchRenderer.clearAll();
        ResourceManager.cleanup();
    }
}
