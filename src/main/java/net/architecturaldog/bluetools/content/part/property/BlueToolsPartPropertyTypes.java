package net.architecturaldog.bluetools.content.part.property;

import com.mojang.serialization.MapCodec;
import dev.jaxydog.lodestone.api.AutoLoaded;
import dev.jaxydog.lodestone.api.AutoLoader;
import dev.jaxydog.lodestone.api.CommonLoaded;
import net.architecturaldog.bluetools.BlueTools;
import net.architecturaldog.bluetools.content.BlueToolsRegistries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

public final class BlueToolsPartPropertyTypes extends AutoLoader {

    public static final @NotNull AutoLoaded<DefaultedPartPropertyType<MaterialValueProperty>> MATERIAL_VALUE =
        BlueToolsPartPropertyTypes.create("material_value", MaterialValueProperty.CODEC, MaterialValueProperty.DEFAULT);
    public static final @NotNull AutoLoaded<DefaultedPartPropertyType<MaterialsProperty>> MATERIALS =
        BlueToolsPartPropertyTypes.create("materials", MaterialsProperty.CODEC, MaterialsProperty.DEFAULT);
    public static final @NotNull AutoLoaded<DefaultedPartPropertyType<RepairableProperty>> REPAIRABLE =
        BlueToolsPartPropertyTypes.create("repairable", RepairableProperty.CODEC, RepairableProperty.DEFAULT);

    private static <P extends PartProperty> @NotNull AutoLoaded<DefaultedPartPropertyType<P>> create(
        final @NotNull String path,
        final @NotNull MapCodec<P> codec,
        final @NotNull P defaultProperty
    )
    {
        return BlueToolsPartPropertyTypes.create(BlueTools.id(path), codec, defaultProperty);
    }

    private static <P extends PartProperty> @NotNull AutoLoaded<DefaultedPartPropertyType<P>> create(
        final @NotNull Identifier identifier,
        final @NotNull MapCodec<P> codec,
        final @NotNull P defaultProperty
    )
    {
        return new AutoLoaded<>(identifier, DefaultedPartPropertyType.of(codec, defaultProperty)).on(
            CommonLoaded.class,
            self -> Registry.register(BlueToolsRegistries.PART_PROPERTY_TYPE, self.getLoaderId(), self.getValue())
        );
    }

    private static <P extends PartProperty> @NotNull AutoLoaded<PartPropertyType<P>> create(
        final @NotNull String path,
        final @NotNull MapCodec<P> codec
    )
    {
        return BlueToolsPartPropertyTypes.create(BlueTools.id(path), codec);
    }

    private static <P extends PartProperty> @NotNull AutoLoaded<PartPropertyType<P>> create(
        final @NotNull Identifier identifier,
        final @NotNull MapCodec<P> codec
    )
    {
        return new AutoLoaded<PartPropertyType<P>>(identifier, () -> codec).on(
            CommonLoaded.class,
            self -> Registry.register(BlueToolsRegistries.PART_PROPERTY_TYPE, self.getLoaderId(), self.getValue())
        );
    }

    @Override
    public Identifier getLoaderId() {
        return BlueTools.id("part_property_types");
    }

}
