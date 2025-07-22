package net.architecturaldog.bluetools.content;

import dev.jaxydog.lodestone.api.AutoLoader;
import net.architecturaldog.bluetools.BlueTools;
import net.architecturaldog.bluetools.content.material.Material;
import net.architecturaldog.bluetools.content.material.MaterialType;
import net.architecturaldog.bluetools.content.material.MiningLevel;
import net.architecturaldog.bluetools.content.material.property.MaterialPropertyType;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

public final class BlueToolsRegistries extends AutoLoader {

    public static final Registry<MaterialPropertyType<?>> MATERIAL_PROPERTY_TYPE =
        FabricRegistryBuilder.createSimple(Keys.MATERIAL_PROPERTY_TYPE).buildAndRegister();

    public static final Registry<MaterialType<?>> MATERIAL_TYPE =
        FabricRegistryBuilder.createSimple(Keys.MATERIAL_TYPE).buildAndRegister();

    @Override
    public Identifier getLoaderId() {
        return BlueTools.id("registries");
    }

    public static final class Keys {

        public static final RegistryKey<Registry<MaterialPropertyType<?>>> MATERIAL_PROPERTY_TYPE =
            RegistryKey.ofRegistry(BlueTools.id("material_property_type"));

        public static final RegistryKey<Registry<MaterialType<?>>> MATERIAL_TYPE =
            RegistryKey.ofRegistry(BlueTools.id("material_type"));

        public static final RegistryKey<Registry<Material>> MATERIAL =
            RegistryKey.ofRegistry(BlueTools.id("material"));

        public static final RegistryKey<Registry<MiningLevel>> MINING_LEVEL =
            RegistryKey.ofRegistry(BlueTools.id("mining_level"));

    }

}
