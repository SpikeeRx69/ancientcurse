package com.ancientcurse.screen;

import com.ancientcurse.AncientCurse;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ClayCrucibleScreen extends HandledScreen<ClayCrucibleScreenHandler> {
    private static final Identifier TEXTURE = new Identifier(AncientCurse.MOD_ID, "textures/gui/clay_crucible_gui.png");

    public ClayCrucibleScreen(ClayCrucibleScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    @Override
    protected void init() {
        super.init();
        // Center the title
        titleX = (backgroundWidth - textRenderer.getWidth(title)) / 2;
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;
        
        // Draw the background
        context.drawTexture(TEXTURE, x, y, 0, 0, backgroundWidth, backgroundHeight);

        // Draw the progress arrow
        int progress = handler.getScaledProgress();
        context.drawTexture(TEXTURE, x + 79, y + 34, 176, 14, progress + 1, 16);

        // Draw the fuel indicator if burning
        if (handler.isBurning()) {
            int fuelProgress = handler.getScaledFuelProgress();
            context.drawTexture(TEXTURE, x + 56, y + 36 + 12 - fuelProgress, 176, 12 - fuelProgress, 14, fuelProgress + 1);
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context);
        super.render(context, mouseX, mouseY, delta);
        drawMouseoverTooltip(context, mouseX, mouseY);
    }
}
