package com.ancientcurse;

import com.ancientcurse.screen.ClayCrucibleScreenHandler;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

/**
 * Centralizes screen handler registration for the mod
 */
public class ModScreenHandlers {
    // Define screen handler types
    public static final ScreenHandlerType<ClayCrucibleScreenHandler> CLAY_CRUCIBLE_SCREEN_HANDLER = 
        new ExtendedScreenHandlerType<ClayCrucibleScreenHandler>((syncId, inventory, buf) -> 
            new ClayCrucibleScreenHandler(syncId, inventory));
    
    /**
     * Registers all screen handlers
     */
    public static void registerScreenHandlers() {
        AncientCurse.LOGGER.info("Registering screen handlers for Ancient Curse");
        
        // Register Clay Crucible screen handler
        Registry.register(
            Registries.SCREEN_HANDLER,
            new Identifier(AncientCurse.MOD_ID, "clay_crucible"),
            CLAY_CRUCIBLE_SCREEN_HANDLER
        );
    }
}
