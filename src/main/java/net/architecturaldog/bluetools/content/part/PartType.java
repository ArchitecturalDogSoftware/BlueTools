package net.architecturaldog.bluetools.content.part;

import com.mojang.serialization.MapCodec;

public interface PartType<P extends Part> {

    MapCodec<P> getCodec();

}
