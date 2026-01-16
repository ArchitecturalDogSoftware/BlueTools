package net.architecturaldog.bluetools.content.item.client;

import net.architecturaldog.bluetools.BlueTools;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class GeneratedPartItemModel extends AbstractGeneratedItemModel {

    public static final GeneratedPartItemModel MODEL_6_LAYERS = new GeneratedPartItemModel(6);

    public GeneratedPartItemModel(final int layerCount) {
        super(layerCount, count -> BlueTools.id("builtin/generated/part_%d_layers".formatted(count)));
    }

}
