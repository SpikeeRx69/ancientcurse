package com.ancientcurse.util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;

/**
 * A simple implementation of {@link Inventory} for block entities.
 * Provides default methods for getting/setting items and inventory manipulation.
 */
public interface ImplementedInventory extends Inventory {
    /**
     * Gets the item list of this inventory.
     * Must return the same instance every time it's called.
     */
    DefaultedList<ItemStack> getItems();
    
    /**
     * Creates an inventory from the item list.
     */
    static ImplementedInventory of(DefaultedList<ItemStack> items) {
        return () -> items;
    }
    
    /**
     * Creates a new inventory with the specified size.
     */
    static ImplementedInventory ofSize(int size) {
        return of(DefaultedList.ofSize(size, ItemStack.EMPTY));
    }
    
    /**
     * Returns the inventory size.
     */
    @Override
    default int size() {
        return getItems().size();
    }
    
    /**
     * Checks if the inventory is empty.
     */
    @Override
    default boolean isEmpty() {
        for (int i = 0; i < size(); i++) {
            if (!getStack(i).isEmpty()) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Gets the item in the specified slot.
     */
    @Override
    default ItemStack getStack(int slot) {
        return getItems().get(slot);
    }
    
    /**
     * Takes a stack of the size from the slot.
     * (default implementation is suitable for most cases)
     */
    @Override
    default ItemStack removeStack(int slot, int count) {
        ItemStack result = Inventories.splitStack(getItems(), slot, count);
        if (!result.isEmpty()) {
            markDirty();
        }
        return result;
    }
    
    /**
     * Removes the current stack in the {@code slot} and returns it.
     */
    @Override
    default ItemStack removeStack(int slot) {
        return Inventories.removeStack(getItems(), slot);
    }
    
    /**
     * Replaces the current stack in the {@code slot} with the provided stack.
     */
    @Override
    default void setStack(int slot, ItemStack stack) {
        getItems().set(slot, stack);
        if (stack.getCount() > getMaxCountPerStack()) {
            stack.setCount(getMaxCountPerStack());
        }
        markDirty();
    }
    
    /**
     * Clears the inventory.
     */
    @Override
    default void clear() {
        getItems().clear();
    }
    
    /**
     * Marks the state as dirty.
     * Must be called after changes in the inventory, so that the game can properly save
     * the inventory contents and notify neighboring blocks of inventory changes.
     */
    @Override
    default void markDirty() {
        // Override if you want behavior.
    }
    
    /**
     * @return true if the player can use the inventory, false otherwise.
     */
    @Override
    default boolean canPlayerUse(PlayerEntity player) {
        return true;
    }
}
