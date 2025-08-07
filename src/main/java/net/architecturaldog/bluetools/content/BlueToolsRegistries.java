package net.architecturaldog.bluetools.content;

import dev.jaxydog.lodestone.api.AutoLoader;
import net.architecturaldog.bluetools.BlueTools;
import net.architecturaldog.bluetools.content.material.Material;
import net.architecturaldog.bluetools.content.material.MaterialIngredient;
import net.architecturaldog.bluetools.content.material.MaterialType;
import net.architecturaldog.bluetools.content.material.MiningLevel;
import net.architecturaldog.bluetools.content.material.property.MaterialPropertyType;
import net.architecturaldog.bluetools.content.part.Part;
import net.architecturaldog.bluetools.content.part.PartType;
import net.architecturaldog.bluetools.content.part.property.PartPropertyType;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

public final class BlueToolsRegistries extends AutoLoader {

    public static final @NotNull Registry<MaterialPropertyType<?>> MATERIAL_PROPERTY_TYPE =
        FabricRegistryBuilder.createSimple(Keys.MATERIAL_PROPERTY_TYPE).buildAndRegister();

    public static final @NotNull Registry<MaterialType<?>> MATERIAL_TYPE =
        FabricRegistryBuilder.createSimple(Keys.MATERIAL_TYPE).buildAndRegister();

    public static final @NotNull Registry<PartPropertyType<?>> PART_PROPERTY_TYPE =
        FabricRegistryBuilder.createSimple(Keys.PART_PROPERTY_TYPE).buildAndRegister();

    public static final @NotNull Registry<PartType<?>> PART_TYPE =
        FabricRegistryBuilder.createSimple(Keys.PART_TYPE).buildAndRegister();

    @Override
    public @NotNull Identifier getLoaderId() {
        return BlueTools.id("registries");
    }

    public static final class Keys {

        public static final @NotNull RegistryKey<Registry<MaterialIngredient>> MATERIAL_INGREDIENT =
            RegistryKey.ofRegistry(BlueTools.id("material_ingredient"));

        public static final @NotNull RegistryKey<Registry<MaterialPropertyType<?>>> MATERIAL_PROPERTY_TYPE =
            RegistryKey.ofRegistry(BlueTools.id("material_property_type"));

        public static final @NotNull RegistryKey<Registry<MaterialType<?>>> MATERIAL_TYPE =
            RegistryKey.ofRegistry(BlueTools.id("material_type"));

        public static final @NotNull RegistryKey<Registry<Material>> MATERIAL =
            RegistryKey.ofRegistry(BlueTools.id("material"));

        public static final @NotNull RegistryKey<Registry<MiningLevel>> MINING_LEVEL =
            RegistryKey.ofRegistry(BlueTools.id("mining_level"));

        public static final @NotNull RegistryKey<Registry<PartPropertyType<?>>> PART_PROPERTY_TYPE =
            RegistryKey.ofRegistry(BlueTools.id("part_property_type"));

        public static final @NotNull RegistryKey<Registry<PartType<?>>> PART_TYPE =
            RegistryKey.ofRegistry(BlueTools.id("part_type"));

        public static final @NotNull RegistryKey<Registry<Part>> PART =
            RegistryKey.ofRegistry(BlueTools.id("part"));

    }

}
