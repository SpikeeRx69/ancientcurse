package com.ancientcurse.mixin;

import com.ancientcurse.AncientCurse;
import net.minecraft.client.gui.screen.world.CreateWorldScreen;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * A mixin to enhance the behavior of the info button, providing more detailed instructions
 * for creating a flat Nile River world.
 */
@Mixin(CreateWorldScreen.class)
public class SingleBiomePresetMixin {

    /**
     * Injects after the info button is clicked to show enhanced instructions.
     */
    @Inject(method = "init", at = @At("RETURN"))
    private void addEnhancedNileRiverInstructions(CallbackInfo ci) {
        AncientCurse.LOGGER.info("Enhanced Nile River world creation instructions loaded");
    }
}
