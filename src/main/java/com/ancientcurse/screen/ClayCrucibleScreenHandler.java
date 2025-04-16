package com.ancientcurse.screen;

import com.ancientcurse.ModScreenHandlers;
import com.ancientcurse.block.entity.ClayCrucibleBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.SmeltingRecipe;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.world.World;

import java.util.Optional;

public class ClayCrucibleScreenHandler extends ScreenHandler {
    private final Inventory inventory;
    private final World world;
    private final PropertyDelegate propertyDelegate;

    public ClayCrucibleScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, new SimpleInventory(3), new ArrayPropertyDelegate(4));
    }

    public ClayCrucibleScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory, PropertyDelegate propertyDelegate) {
        super(ModScreenHandlers.CLAY_CRUCIBLE_SCREEN_HANDLER, syncId);
        checkSize(inventory, 3);
        this.inventory = inventory;
        this.world = playerInventory.player.getWorld();
        this.propertyDelegate = propertyDelegate;
        
        // Add properties for tracking progress and fuel
        this.addProperties(propertyDelegate);

        // Add crucible inventory slots
        // Input slot
        this.addSlot(new Slot(inventory, ClayCrucibleBlockEntity.INPUT_SLOT, 56, 17) {
            @Override
            public boolean canInsert(ItemStack stack) {
                return canSmelt(stack);
            }
        });
        
        // Fuel slot
        this.addSlot(new FuelSlot(inventory, ClayCrucibleBlockEntity.FUEL_SLOT, 56, 53));
        
        // Output slot
        this.addSlot(new OutputSlot(inventory, ClayCrucibleBlockEntity.OUTPUT_SLOT, 116, 35));

        // Add player inventory slots
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                this.addSlot(new Slot(playerInventory, col + row * 9 + 9, 8 + col * 18, 84 + row * 18));
            }
        }

        // Add player hotbar slots
        for (int col = 0; col < 9; ++col) {
            this.addSlot(new Slot(playerInventory, col, 8 + col * 18, 142));
        }
    }

    // Check if an item can be smelted
    private boolean canSmelt(ItemStack stack) {
        SimpleInventory testInventory = new SimpleInventory(1);
        testInventory.setStack(0, stack);
        return world.getRecipeManager().getFirstMatch(RecipeType.SMELTING, testInventory, world).isPresent();
    }

    // Get current progress
    public int getScaledProgress() {
        int progress = this.propertyDelegate.get(0);
        int maxProgress = this.propertyDelegate.get(1);
        int progressArrowSize = 24; // Width of the arrow texture

        return maxProgress != 0 && progress != 0 ? progress * progressArrowSize / maxProgress : 0;
    }

    // Get current fuel level
    public int getScaledFuelProgress() {
        int fuelTime = this.propertyDelegate.get(2);
        int maxFuelTime = this.propertyDelegate.get(3);
        int fuelProgressSize = 14; // Height of the fuel indicator

        return maxFuelTime != 0 ? fuelTime * fuelProgressSize / maxFuelTime : 0;
    }

    // Check if currently burning
    public boolean isBurning() {
        return propertyDelegate.get(2) > 0;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return this.inventory.canPlayerUse(player);
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int invSlot) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(invSlot);
        
        if (slot != null && slot.hasStack()) {
            ItemStack originalStack = slot.getStack();
            newStack = originalStack.copy();
            
            if (invSlot < this.inventory.size()) {
                // If the slot is in the crucible inventory, move to player inventory
                if (!this.insertItem(originalStack, this.inventory.size(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else {
                // If the slot is in the player inventory, move to crucible inventory
                if (canSmelt(originalStack)) {
                    // Try to move to input slot
                    if (!this.insertItem(originalStack, ClayCrucibleBlockEntity.INPUT_SLOT, ClayCrucibleBlockEntity.INPUT_SLOT + 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (FuelSlot.isFuel(originalStack)) {
                    // Try to move to fuel slot
                    if (!this.insertItem(originalStack, ClayCrucibleBlockEntity.FUEL_SLOT, ClayCrucibleBlockEntity.FUEL_SLOT + 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (invSlot >= this.inventory.size() && invSlot < this.slots.size() - 9) {
                    // Move from main inventory to hotbar
                    if (!this.insertItem(originalStack, this.slots.size() - 9, this.slots.size(), false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (invSlot >= this.slots.size() - 9 && invSlot < this.slots.size()) {
                    // Move from hotbar to main inventory
                    if (!this.insertItem(originalStack, this.inventory.size(), this.slots.size() - 9, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            }

            if (originalStack.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }

            if (originalStack.getCount() == newStack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTakeItem(player, originalStack);
        }

        return newStack;
    }

    // Custom slot for fuel items
    static class FuelSlot extends Slot {
        public FuelSlot(Inventory inventory, int index, int x, int y) {
            super(inventory, index, x, y);
        }

        @Override
        public boolean canInsert(ItemStack stack) {
            return isFuel(stack);
        }

        public static boolean isFuel(ItemStack stack) {
            // This is a simple check - in a real implementation you'd use the RecipeManager
            // For now we'll just check if it's a common fuel item
            return stack.isIn(net.minecraft.registry.tag.ItemTags.COALS) || 
                   stack.isOf(net.minecraft.item.Items.BLAZE_ROD) ||
                   stack.isOf(net.minecraft.item.Items.LAVA_BUCKET);
        }
    }

    // Custom slot for output items
    static class OutputSlot extends Slot {
        public OutputSlot(Inventory inventory, int index, int x, int y) {
            super(inventory, index, x, y);
        }

        @Override
        public boolean canInsert(ItemStack stack) {
            return false; // Cannot insert into output slot
        }
    }
}
