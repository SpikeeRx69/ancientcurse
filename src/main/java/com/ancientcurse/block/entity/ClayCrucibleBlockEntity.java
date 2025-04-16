package com.ancientcurse.block.entity;

import com.ancientcurse.ModBlockEntities;
import com.ancientcurse.block.ClayCrucibleBlock;
import com.ancientcurse.screen.ClayCrucibleScreenHandler;
import com.ancientcurse.util.ImplementedInventory;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.SmeltingRecipe;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class ClayCrucibleBlockEntity extends BlockEntity implements NamedScreenHandlerFactory, ImplementedInventory {
    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(3, ItemStack.EMPTY);
    
    // Slot indices
    public static final int INPUT_SLOT = 0;
    public static final int FUEL_SLOT = 1;
    public static final int OUTPUT_SLOT = 2;
    
    // Crucible properties
    protected final PropertyDelegate propertyDelegate;
    private int progress = 0;
    private int maxProgress = 72; // Slightly slower than a regular furnace
    private int fuelTime = 0;
    private int maxFuelTime = 0;

    public ClayCrucibleBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.CLAY_CRUCIBLE, pos, state);
        this.propertyDelegate = new PropertyDelegate() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> ClayCrucibleBlockEntity.this.progress;
                    case 1 -> ClayCrucibleBlockEntity.this.maxProgress;
                    case 2 -> ClayCrucibleBlockEntity.this.fuelTime;
                    case 3 -> ClayCrucibleBlockEntity.this.maxFuelTime;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> ClayCrucibleBlockEntity.this.progress = value;
                    case 1 -> ClayCrucibleBlockEntity.this.maxProgress = value;
                    case 2 -> ClayCrucibleBlockEntity.this.fuelTime = value;
                    case 3 -> ClayCrucibleBlockEntity.this.maxFuelTime = value;
                }
            }

            @Override
            public int size() {
                return 4;
            }
        };
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return inventory;
    }

    @Override
    public Text getDisplayName() {
        return Text.translatable("block.ancientcurse.clay_crucible");
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new ClayCrucibleScreenHandler(syncId, playerInventory, this, this.propertyDelegate);
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        Inventories.writeNbt(nbt, inventory);
        nbt.putInt("clay_crucible.progress", progress);
        nbt.putInt("clay_crucible.fuelTime", fuelTime);
        nbt.putInt("clay_crucible.maxFuelTime", maxFuelTime);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        Inventories.readNbt(nbt, inventory);
        progress = nbt.getInt("clay_crucible.progress");
        fuelTime = nbt.getInt("clay_crucible.fuelTime");
        maxFuelTime = nbt.getInt("clay_crucible.maxFuelTime");
    }

    public static void tick(World world, BlockPos pos, BlockState state, ClayCrucibleBlockEntity entity) {
        if (world.isClient()) {
            return;
        }

        boolean wasLit = state.get(ClayCrucibleBlock.LIT);
        boolean shouldBeLit = entity.isBurning();
        
        // Update lit state if needed
        if (wasLit != shouldBeLit) {
            world.setBlockState(pos, state.with(ClayCrucibleBlock.LIT, shouldBeLit), 3);
        }

        // Handle fuel
        if (entity.isBurning()) {
            entity.fuelTime--;
        }

        // Get recipe and check if we can process
        Optional<SmeltingRecipe> recipe = getRecipe(entity);
        
        if (recipe.isPresent() && canProcess(recipe.get(), entity)) {
            // If we need fuel, try to consume it
            if (!entity.isBurning()) {
                entity.consumeFuel();
            }

            // If we're burning, process the recipe
            if (entity.isBurning()) {
                entity.progress++;
                
                // If we've reached max progress, craft the item
                if (entity.progress >= entity.maxProgress) {
                    entity.craftItem(recipe.get());
                    entity.progress = 0;
                }
            } else {
                // Reset progress if not burning
                entity.progress = 0;
            }
        } else {
            // Reset progress if recipe is invalid
            entity.progress = 0;
        }
    }

    private static Optional<SmeltingRecipe> getRecipe(ClayCrucibleBlockEntity entity) {
        World world = entity.world;
        SimpleInventory inventory = new SimpleInventory(entity.size());
        
        for (int i = 0; i < entity.size(); i++) {
            inventory.setStack(i, entity.getStack(i));
        }

        return world.getRecipeManager().getFirstMatch(RecipeType.SMELTING, inventory, world);
    }

    private static boolean canProcess(SmeltingRecipe recipe, ClayCrucibleBlockEntity entity) {
        ItemStack resultStack = recipe.getOutput(entity.world.getRegistryManager());
        
        // Check if output slot is empty or can stack with result
        if (entity.getStack(OUTPUT_SLOT).isEmpty()) {
            return true;
        }
        
        ItemStack outputStack = entity.getStack(OUTPUT_SLOT);
        if (!ItemStack.areItemsEqual(outputStack, resultStack)) {
            return false;
        }
        
        // Check if there's room to add the result
        return outputStack.getCount() + resultStack.getCount() <= outputStack.getMaxCount();
    }

    private void craftItem(SmeltingRecipe recipe) {
        ItemStack resultStack = recipe.getOutput(world.getRegistryManager());
        
        // Consume input
        this.removeStack(INPUT_SLOT, 1);
        
        // Add to output
        ItemStack outputStack = this.getStack(OUTPUT_SLOT);
        if (outputStack.isEmpty()) {
            this.setStack(OUTPUT_SLOT, resultStack.copy());
        } else if (ItemStack.areItemsEqual(outputStack, resultStack)) {
            outputStack.increment(resultStack.getCount());
        }
    }

    private void consumeFuel() {
        ItemStack fuelStack = this.getStack(FUEL_SLOT);
        
        if (!fuelStack.isEmpty()) {
            // Simple check for fuel items
            int burnTime = getBurnTime(fuelStack);
            if (burnTime > 0) {
                this.fuelTime = burnTime;
                this.maxFuelTime = burnTime;
                
                // Consume the fuel item
                Item remainingItem = fuelStack.getItem().getRecipeRemainder();
                fuelStack.decrement(1);
                
                // Add remaining item if applicable (like empty buckets)
                if (!fuelStack.isEmpty() && remainingItem != null) {
                    if (this.getStack(FUEL_SLOT).isEmpty()) {
                        this.setStack(FUEL_SLOT, new ItemStack(remainingItem));
                    }
                }
            }
        }
    }

    // Simple helper method to determine burn time for common fuels
    private int getBurnTime(ItemStack stack) {
        if (stack.isIn(net.minecraft.registry.tag.ItemTags.COALS)) {
            return 1600; // Standard coal burn time
        } else if (stack.isOf(net.minecraft.item.Items.BLAZE_ROD)) {
            return 2400; // Blaze rod burn time
        } else if (stack.isOf(net.minecraft.item.Items.LAVA_BUCKET)) {
            return 20000; // Lava bucket burn time
        }
        return 0;
    }

    public boolean isBurning() {
        return this.fuelTime > 0;
    }
}
