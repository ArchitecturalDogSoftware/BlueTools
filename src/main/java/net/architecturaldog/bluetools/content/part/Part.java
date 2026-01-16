package net.architecturaldog.bluetools.content.part;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import com.mojang.serialization.Codec;

import net.architecturaldog.bluetools.content.BlueToolsRegistries;
import net.architecturaldog.bluetools.content.part.property.DefaultedPartPropertyType;
import net.architecturaldog.bluetools.content.part.property.PartProperty;
import net.architecturaldog.bluetools.content.part.property.PartPropertyType;

public interface Part {

    Codec<Part> CODEC = BlueToolsRegistries.PART_TYPE.getCodec().dispatch(Part::getType, PartType::getCodec);

    PartType<? extends Part> getType();

    boolean hasProperty(final PartPropertyType<?> type);

    <P extends PartProperty> Optional<P> getProperty(final PartPropertyType<P> type);

    List<PartProperty> getProperties();

    default <P extends PartProperty> P getPropertyOr(final PartPropertyType<P> type, final P property) {
        return this.getProperty(type).orElse(property);
    }

    default <P extends PartProperty> P getPropertyOrElse(final PartPropertyType<P> type, final Supplier<P> supplier) {
        return this.getProperty(type).orElseGet(supplier);
    }

    default <P extends PartProperty> P getPropertyOrDefault(final DefaultedPartPropertyType<P> type) {
        return this.getPropertyOrElse(type, type::getDefault);
    }

}
