package net.architecturaldog.bluetools.content.material.property;

import com.mojang.serialization.MapCodec;
import dev.jaxydog.lodestone.api.AutoLoaded;
import dev.jaxydog.lodestone.api.AutoLoader;
import net.architecturaldog.bluetools.BlueTools;
import net.architecturaldog.bluetools.content.BlueToolsRegistries;
import net.architecturaldog.bluetools.utility.RegistryLoaded;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

public final class BlueToolsMaterialPropertyTypes extends AutoLoader {

    public static final @NotNull AutoLoaded<MaterialPropertyType<ArmorDefenseProperty>> ARMOR_DEFENSE =
        BlueToolsMaterialPropertyTypes.create("armor_defense", ArmorDefenseProperty.CODEC);
    public static final @NotNull AutoLoaded<MaterialPropertyType<ArmorKnockbackResistanceProperty>> ARMOR_KNOCKBACK_RESISTANCE =
        BlueToolsMaterialPropertyTypes.create("armor_knockback_resistance", ArmorKnockbackResistanceProperty.CODEC);
    public static final @NotNull AutoLoaded<MaterialPropertyType<ArmorToughnessProperty>> ARMOR_TOUGHNESS =
        BlueToolsMaterialPropertyTypes.create("armor_toughness", ArmorToughnessProperty.CODEC);
    public static final @NotNull AutoLoaded<MaterialPropertyType<AttackDamageProperty>> ATTACK_DAMAGE =
        BlueToolsMaterialPropertyTypes.create("attack_damage", AttackDamageProperty.CODEC);
    public static final @NotNull AutoLoaded<MaterialPropertyType<AttackSpeedProperty>> ATTACK_SPEED =
        BlueToolsMaterialPropertyTypes.create("attack_speed", AttackSpeedProperty.CODEC);
    public static final @NotNull AutoLoaded<MaterialPropertyType<ColorProperty>> COLOR =
        BlueToolsMaterialPropertyTypes.create("color", ColorProperty.CODEC);
    public static final @NotNull AutoLoaded<MaterialPropertyType<DurabilityProperty>> DURABILITY =
        BlueToolsMaterialPropertyTypes.create("durability", DurabilityProperty.CODEC);
    public static final @NotNull AutoLoaded<MaterialPropertyType<FluidProperty>> FLUID =
        BlueToolsMaterialPropertyTypes.create("fluid", FluidProperty.CODEC);
    public static final @NotNull AutoLoaded<MaterialPropertyType<MiningLevelProperty>> MINING_LEVEL =
        BlueToolsMaterialPropertyTypes.create("mining_level", MiningLevelProperty.CODEC);
    public static final @NotNull AutoLoaded<MaterialPropertyType<MiningSpeedProperty>> MINING_SPEED =
        BlueToolsMaterialPropertyTypes.create("mining_speed", MiningSpeedProperty.CODEC);

    private static <P extends MaterialProperty> @NotNull AutoLoaded<MaterialPropertyType<P>> create(
        final @NotNull String path,
        final @NotNull MapCodec<P> codec
    )
    {
        return BlueToolsMaterialPropertyTypes.create(BlueTools.id(path), codec);
    }

    private static <P extends MaterialProperty> @NotNull AutoLoaded<MaterialPropertyType<P>> create(
        final @NotNull Identifier identifier,
        final @NotNull MapCodec<P> codec
    )
    {
        return new RegistryLoaded<>(identifier, BlueToolsRegistries.MATERIAL_PROPERTY_TYPE, () -> codec);
    }

    @Override
    public @NotNull Identifier getLoaderId() {
        return BlueTools.id("property_types");
    }

}
