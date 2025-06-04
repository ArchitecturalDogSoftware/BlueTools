package net.architecturaldog.bluetools.content.material.property;

import com.mojang.serialization.MapCodec;

public interface MaterialProperty {

    MaterialPropertyType<? extends MaterialProperty> getType();

    default MapCodec<? extends MaterialProperty> getCodec() {
        return this.getType().getCodec();
    }

}
