package net.architecturaldog.bluetools.content.material.property;

import com.mojang.serialization.MapCodec;
import dev.jaxydog.lodestone.api.AutoLoaded;
import dev.jaxydog.lodestone.api.AutoLoader;
import dev.jaxydog.lodestone.api.CommonLoaded;
import net.architecturaldog.bluetools.BlueTools;
import net.architecturaldog.bluetools.content.BlueToolsRegistries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public final class BlueToolsMaterialPropertyTypes extends AutoLoader {

    public static final @NotNull AutoLoaded<MaterialPropertyType<ArmorDefenseProperty>> ARMOR_DEFENSE =
        BlueToolsMaterialPropertyTypes.create("armor_defense", ArmorDefenseProperty.CODEC);
    public static final @NotNull AutoLoaded<DefaultedMaterialPropertyType<ArmorKnockbackResistanceProperty>> ARMOR_KNOCKBACK_RESISTANCE =
        BlueToolsMaterialPropertyTypes.create(
            "armor_knockback_resistance",
            ArmorKnockbackResistanceProperty.CODEC,
            () -> ArmorKnockbackResistanceProperty.DEFAULT
        );
    public static final @NotNull AutoLoaded<DefaultedMaterialPropertyType<ArmorToughnessProperty>> ARMOR_TOUGHNESS =
        BlueToolsMaterialPropertyTypes.create(
            "armor_toughness",
            ArmorToughnessProperty.CODEC,
            () -> ArmorToughnessProperty.DEFAULT
        );
    public static final @NotNull AutoLoaded<DefaultedMaterialPropertyType<AttackDamageProperty>> ATTACK_DAMAGE =
        BlueToolsMaterialPropertyTypes.create(
            "attack_damage",
            AttackDamageProperty.CODEC,
            () -> AttackDamageProperty.DEFAULT
        );
    public static final @NotNull AutoLoaded<DefaultedMaterialPropertyType<AttackSpeedProperty>> ATTACK_SPEED =
        BlueToolsMaterialPropertyTypes.create(
            "attack_speed",
            AttackSpeedProperty.CODEC,
            () -> AttackSpeedProperty.DEFAULT
        );
    public static final @NotNull AutoLoaded<MaterialPropertyType<ColorProperty>> COLOR =
        BlueToolsMaterialPropertyTypes.create("color", ColorProperty.CODEC);
    public static final @NotNull AutoLoaded<MaterialPropertyType<DurabilityProperty>> DURABILITY =
        BlueToolsMaterialPropertyTypes.create("durability", DurabilityProperty.CODEC);
    public static final @NotNull AutoLoaded<MaterialPropertyType<FluidProperty>> FLUID =
        BlueToolsMaterialPropertyTypes.create("fluid", FluidProperty.CODEC);
    public static final @NotNull AutoLoaded<DefaultedMaterialPropertyType<MiningLevelProperty>> MINING_LEVEL =
        BlueToolsMaterialPropertyTypes.create(
            "mining_level",
            MiningLevelProperty.CODEC,
            MiningLevelProperty::getDefault
        );
    public static final @NotNull AutoLoaded<DefaultedMaterialPropertyType<MiningSpeedProperty>> MINING_SPEED =
        BlueToolsMaterialPropertyTypes.create(
            "mining_speed",
            MiningSpeedProperty.CODEC,
            () -> MiningSpeedProperty.DEFAULT
        );

    private static <P extends MaterialProperty> @NotNull AutoLoaded<DefaultedMaterialPropertyType<P>> create(
        final @NotNull String path,
        final @NotNull MapCodec<P> codec,
        final @NotNull Supplier<P> defaultProperty
    )
    {
        return BlueToolsMaterialPropertyTypes.create(BlueTools.id(path), codec, defaultProperty);
    }

    private static <P extends MaterialProperty> @NotNull AutoLoaded<DefaultedMaterialPropertyType<P>> create(
        final @NotNull Identifier identifier,
        final @NotNull MapCodec<P> codec,
        final @NotNull Supplier<P> defaultProperty
    )
    {
        return new AutoLoaded<>(identifier, DefaultedMaterialPropertyType.of(codec, defaultProperty)).on(
            CommonLoaded.class,
            self -> Registry.register(BlueToolsRegistries.MATERIAL_PROPERTY_TYPE, self.getLoaderId(), self.getValue())
        );
    }

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
        return new AutoLoaded<MaterialPropertyType<P>>(identifier, () -> codec).on(
            CommonLoaded.class,
            self -> Registry.register(BlueToolsRegistries.MATERIAL_PROPERTY_TYPE, self.getLoaderId(), self.getValue())
        );
    }

    @Override
    public @NotNull Identifier getLoaderId() {
        return BlueTools.id("material_property_types");
    }

}
