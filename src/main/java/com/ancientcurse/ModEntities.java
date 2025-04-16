package com.ancientcurse;

import com.ancientcurse.entity.SunGolemEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

/**
 * Centralizes entity registration for the mod
 */
public class ModEntities {
    
    // Example entity registration (commented out until ready to implement)
    /*
    public static final EntityType<SunGolemEntity> SUN_GOLEM = Registry.register(
        Registries.ENTITY_TYPE,
        new Identifier(AncientCurse.MOD_ID, "sun_golem"),
        FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, SunGolemEntity::new)
            .dimensions(EntityDimensions.fixed(0.9f, 1.8f))
            .build()
    );
    */
    
    /**
     * Registers all mod entities
     */
    public static void registerEntities() {
        AncientCurse.LOGGER.info("Registering entities for " + AncientCurse.MOD_ID);
        
        // When you're ready to implement the entity, uncomment the code above
        // and then uncomment the following code for client-side rendering:
        
        /*
        EntityRendererRegistry.register(SUN_GOLEM, SunGolemRenderer::new);
        
        // Register spawn egg item
        Registry.register(
            Registries.ITEM, 
            new Identifier(AncientCurse.MOD_ID, "sun_golem_spawn_egg"),
            new SpawnEggItem(SUN_GOLEM, 0xF7C649, 0xFFAE19, new Item.Settings())
        );
        */
    }
} 