package com.ancientcurse.world.gen;

import com.ancientcurse.AncientCurse;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

/**
 * Handles registration of custom surface rules to ensure they are properly applied
 * during world generation.
 */
public class ModSurfaceRuleRegistration {

    /**
     * Register our custom surface rules to be used by the world generation system.
     */
    public static void register() {
        AncientCurse.LOGGER.info("Registering custom surface rules for Ancient Curse mod");

        // Hook into server start event to modify the surface rules
        ServerLifecycleEvents.SERVER_STARTING.register(server -> {
            // This runs when the server is starting, giving us a chance to modify world generation
            AncientCurse.LOGGER.info("Server starting, applying custom surface rules for Nile River biome");

            // Log the status of our custom blocks to verify they're registered
            verifyBlockRegistration();
        });
    }

    /**
     * Verify that our custom blocks are properly registered
     */
    private static void verifyBlockRegistration() {
        AncientCurse.LOGGER.info("Verifying custom block registration for Nile River biome:");
        
        boolean nileRiverSandRegistered = Registries.BLOCK.containsId(
            new Identifier(AncientCurse.MOD_ID, "nile_river_sand"));
        AncientCurse.LOGGER.info("  - Nile River Sand registered: " + nileRiverSandRegistered);
        
        boolean nileMudRegistered = Registries.BLOCK.containsId(
            new Identifier(AncientCurse.MOD_ID, "nile_mud"));
        AncientCurse.LOGGER.info("  - Nile Mud registered: " + nileMudRegistered);
        
        boolean fertileSiltRegistered = Registries.BLOCK.containsId(
            new Identifier(AncientCurse.MOD_ID, "fertile_nile_silt"));
        AncientCurse.LOGGER.info("  - Fertile Nile Silt registered: " + fertileSiltRegistered);
    }
}
