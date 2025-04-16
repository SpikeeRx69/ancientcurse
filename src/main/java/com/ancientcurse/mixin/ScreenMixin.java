package com.ancientcurse.mixin;

import com.ancientcurse.AncientCurse;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Screen mixin for tracking screen transitions
 */
@Mixin(Screen.class)
public class ScreenMixin {
    
    /**
     * Log when certain screens are removed
     */
    @Inject(method = "removed", at = @At("HEAD"))
    private void onScreenRemoved(CallbackInfo ci) {
        Screen self = (Screen)(Object)this;
        
        // Log when significant screens are closed
        if (self instanceof TitleScreen) {
            AncientCurse.LOGGER.info("Leaving title screen");
        } else if (self instanceof CreateWorldScreen) {
            AncientCurse.LOGGER.info("Leaving world creation screen");
        }
    }
} 