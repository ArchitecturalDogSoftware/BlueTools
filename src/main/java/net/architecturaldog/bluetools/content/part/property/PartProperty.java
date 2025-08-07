package net.architecturaldog.bluetools.content.part.property;

import com.mojang.serialization.Codec;
import net.architecturaldog.bluetools.content.BlueToolsRegistries;
import org.jetbrains.annotations.NotNull;

public interface PartProperty {

    @NotNull Codec<PartProperty> CODEC = BlueToolsRegistries.PART_PROPERTY_TYPE
        .getCodec()
        .dispatch(PartProperty::getType, PartPropertyType::getCodec);

    @NotNull PartPropertyType<? extends PartProperty> getType();

}
