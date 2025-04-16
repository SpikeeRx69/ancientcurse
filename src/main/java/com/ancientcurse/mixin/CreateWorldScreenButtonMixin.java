package com.ancientcurse.mixin;

import com.ancientcurse.AncientCurse;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Adds an informational button to guide players on creating a flat Nile River world 
 */
@Mixin(CreateWorldScreen.class)
public abstract class CreateWorldScreenButtonMixin extends Screen {

    @Unique
    private ButtonWidget ancientCurseInfoButton;

    protected CreateWorldScreenButtonMixin(Text title) {
        super(title);
    }

    @Inject(method = "init", at = @At("RETURN"))
    private void ancientcurse_addInfoButton(CallbackInfo ci) {
        int buttonWidth = 260;
        int buttonHeight = 20;
        int x = this.width / 2 - buttonWidth / 2;
        int y = 180;

        ancientCurseInfoButton = ButtonWidget.builder(
                Text.literal("ⓘ For Completely Flat Nile River: Click Here"),
                (button) -> ancientcurse_showInstructions()
            )
            .position(x, y)
            .size(buttonWidth, buttonHeight)
            .build();

        this.addDrawableChild(ancientCurseInfoButton);
        AncientCurse.LOGGER.info("Added Ancient Curse info button to CreateWorldScreen");
    }

    @Unique
    private void ancientcurse_showInstructions() {
        // Display info by changing button text temporarily
        Text originalText = ancientCurseInfoButton.getMessage();
        ancientCurseInfoButton.setMessage(Text.literal("→ Create World > More > World Type > Choose 'flat_nile_desert'"));
        
        // Schedule reset of button text after 5 seconds
        MinecraftClient.getInstance().execute(() -> {
            try {
                Thread.sleep(5000);
                ancientCurseInfoButton.setMessage(originalText);
            } catch (InterruptedException e) {
                // Ignore
            }
        });
    }
} 