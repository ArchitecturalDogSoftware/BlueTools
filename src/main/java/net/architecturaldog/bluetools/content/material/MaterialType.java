package net.architecturaldog.bluetools.content.material;

import com.mojang.serialization.MapCodec;

public interface MaterialType<M extends Material> {

    MapCodec<M> getCodec();

}
