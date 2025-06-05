package net.architecturaldog.bluetools.content;

import dev.jaxydog.lodestone.api.AutoLoader;
import net.architecturaldog.bluetools.BlueTools;
import net.architecturaldog.bluetools.content.material.Material;
import net.architecturaldog.bluetools.content.material.MaterialAlloyingRecipe;
import net.architecturaldog.bluetools.content.material.MaterialMiningLevel;
import net.architecturaldog.bluetools.content.material.property.MaterialPropertyType;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

public final class BlueToolsRegistries extends AutoLoader {

    public static final Registry<MaterialPropertyType<?>> MATERIAL_PROPERTY_TYPE = FabricRegistryBuilder
        .createSimple(BlueToolsRegistries.Keys.MATERIAL_PROPERTY_TYPE)
        .buildAndRegister();

    @Override
    public Identifier getLoaderId() {
        return BlueTools.id("registries");
    }

    public static final class Keys {

        public static final RegistryKey<Registry<Material>> MATERIAL =
            RegistryKey.ofRegistry(BlueTools.id("material"));

        public static final RegistryKey<Registry<MaterialAlloyingRecipe>> MATERIAL_ALLOYING_RECIPE =
            RegistryKey.ofRegistry(BlueTools.id("material_alloying_recipe"));

        public static final RegistryKey<Registry<MaterialMiningLevel>> MATERIAL_MINING_LEVEL =
            RegistryKey.ofRegistry(BlueTools.id("material_mining_level"));

        public static final RegistryKey<Registry<MaterialPropertyType<?>>> MATERIAL_PROPERTY_TYPE =
            RegistryKey.ofRegistry(BlueTools.id("material_property_type"));

    }

}
