package net.architecturaldog.bluetools.content.material;

import dev.jaxydog.lodestone.api.AutoLoader;
import net.architecturaldog.bluetools.BlueTools;
import net.architecturaldog.bluetools.content.BlueToolsRegistries;
import net.architecturaldog.bluetools.content.material.property.*;
import net.architecturaldog.bluetools.utility.RegistryLoaded;
import net.minecraft.util.Identifier;

public final class BlueToolsMaterialPropertyTypes extends AutoLoader {

    public static final RegistryLoaded<MaterialPropertyType<ArmorDataProperty>> ARMOR_DATA =
        create("armor_data", MaterialPropertyType.simple(ArmorDataProperty.CODEC));

    public static final RegistryLoaded<MaterialPropertyType<DurabilityProperty>> DURABILITY =
        create("durability", MaterialPropertyType.simple(DurabilityProperty.CODEC));

    public static final RegistryLoaded<MaterialPropertyType<FluidDataProperty>> FLUID_DATA =
        create("fluid_data", MaterialPropertyType.simple(FluidDataProperty.CODEC));

    public static final RegistryLoaded<MaterialPropertyType<ItemDataProperty>> ITEM_DATA =
        create("item_data", MaterialPropertyType.simple(ItemDataProperty.CODEC));

    public static final RegistryLoaded<MaterialPropertyType<ToolDataProperty>> TOOL_DATA =
        create("tool_data", MaterialPropertyType.simple(ToolDataProperty.CODEC));

    public static final RegistryLoaded<MaterialPropertyType<WeaponDataProperty>> WEAPON_DATA =
        create("weapon_data", MaterialPropertyType.simple(WeaponDataProperty.CODEC));

    private static <T extends MaterialPropertyType<?>> RegistryLoaded<T> create(final String path, final T value) {
        return new RegistryLoaded<>(path, BlueToolsRegistries.MATERIAL_PROPERTY_TYPE, value);
    }

    @Override
    public Identifier getLoaderId() {
        return BlueTools.id("material_property_types");
    }

}
