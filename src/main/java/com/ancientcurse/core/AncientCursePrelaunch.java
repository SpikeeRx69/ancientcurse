package com.ancientcurse.core;

import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * PreLaunch entry point that runs before the game fully initializes.
 * We need to be careful not to access Minecraft classes or registries here
 * as they are not bootstrapped yet.
 */
public class AncientCursePrelaunch implements PreLaunchEntrypoint {
    private static final Logger LOGGER = LoggerFactory.getLogger("AncientCurse-PreLaunch");

    @Override
    public void onPreLaunch() {
        // Simply log that we're in pre-launch without trying to access Minecraft registries
        LOGGER.info("Ancient Curse mod pre-launch phase starting");
        // We'll handle all registry initialization in the main mod initializer instead
        LOGGER.info("Registry initialization will be handled during main initialization");
    }
}
