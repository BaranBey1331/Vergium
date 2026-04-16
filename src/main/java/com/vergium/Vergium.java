package com.vergium;

import com.vergium.core.render.ResourceManager;
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
        // Clean cleanup on JVM shutdown
        Runtime.getRuntime().addShutdownHook(new Thread(ResourceManager::cleanup));
        LOGGER.info("Vergium optimization mod initialized!");
    }

    private void setupClient(final FMLClientSetupEvent event) {
        // Client-side setup logic
    }
}
