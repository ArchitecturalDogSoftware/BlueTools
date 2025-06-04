package net.architecturaldog.bluetools.content.material.property;

import com.mojang.serialization.MapCodec;

public record SimpleMaterialPropertyType<P extends MaterialProperty>(MapCodec<P> codec)
    implements MaterialPropertyType<P>
{

    @Override
    public MapCodec<P> getCodec() {
        return this.codec();
    }

}
