package com.ancientcurse.util;

import com.ancientcurse.AncientCurse;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.block.Blocks;

/**
 * Helper class to ensure proper registration of block states
 * to prevent "Some intrusive holders were not registered" errors.
 */
public class BlockStateRegistrationHelper {

    /**
     * Register this helper to be called at the appropriate lifecycle events
     */
    public static void register() {
        // Register server starting event handler
        ServerLifecycleEvents.SERVER_STARTING.register(server -> {
            preRegisterBlockStates();
        });
    }
    
    /**
     * Pre-registers commonly used block states to ensure they are properly
     * registered before world generation starts.
     */
    private static void preRegisterBlockStates() {
        AncientCurse.LOGGER.info("Pre-registering vanilla block states to prevent registry errors");
        try {
            // Force air block registration
            Blocks.AIR.getDefaultState();
            // Force other commonly used blocks
            Blocks.WATER.getDefaultState();
            Blocks.STONE.getDefaultState();
            Blocks.SAND.getDefaultState();
            Blocks.DIRT.getDefaultState();
            Blocks.GRASS_BLOCK.getDefaultState();
            
            AncientCurse.LOGGER.info("Successfully pre-registered vanilla block states");
        } catch (Exception e) {
            AncientCurse.LOGGER.error("Error during block state pre-registration", e);
        }
    }
}
