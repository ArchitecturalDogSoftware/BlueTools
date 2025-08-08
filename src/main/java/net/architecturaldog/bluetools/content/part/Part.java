package net.architecturaldog.bluetools.content.part;

import com.mojang.serialization.Codec;
import net.architecturaldog.bluetools.content.BlueToolsRegistries;
import net.architecturaldog.bluetools.content.part.property.PartProperty;
import net.architecturaldog.bluetools.content.part.property.PartPropertyType;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public interface Part {

    @NotNull Codec<Part> CODEC = BlueToolsRegistries.PART_TYPE.getCodec().dispatch(Part::getType, PartType::getCodec);

    @NotNull PartType<? extends Part> getType();

    boolean hasProperty(final @NotNull PartPropertyType<?> type);

    <P extends PartProperty> @NotNull Optional<P> getProperty(final @NotNull PartPropertyType<P> type);

    @NotNull List<PartProperty> getProperties();

    default <P extends PartProperty> @NotNull P getPropertyOr(
        final @NotNull PartPropertyType<P> type,
        final @NotNull P property
    )
    {
        return this.getProperty(type).orElse(property);
    }

    default <P extends PartProperty> @NotNull P getPropertyOrElse(
        final @NotNull PartPropertyType<P> type,
        final @NotNull Supplier<P> supplier
    )
    {
        return this.getProperty(type).orElseGet(supplier);
    }

}
