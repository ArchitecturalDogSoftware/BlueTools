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

    public static final @NotNull AutoLoaded<PartPropertyType<MaterialValueProperty>> MATERIAL_VALUE =
        BlueToolsPartPropertyTypes.create("material_value", MaterialValueProperty.CODEC);
    public static final @NotNull AutoLoaded<PartPropertyType<MaterialsProperty>> MATERIALS =
        BlueToolsPartPropertyTypes.create("materials", MaterialsProperty.CODEC);
    public static final @NotNull AutoLoaded<PartPropertyType<RepairableProperty>> REPAIRABLE =
        BlueToolsPartPropertyTypes.create("repairable", RepairableProperty.CODEC);

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
