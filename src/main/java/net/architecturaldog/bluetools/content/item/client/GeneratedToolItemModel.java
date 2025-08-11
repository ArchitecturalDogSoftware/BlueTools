package net.architecturaldog.bluetools.content.item.client;

import net.architecturaldog.bluetools.BlueTools;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.jetbrains.annotations.NotNull;

@Environment(EnvType.CLIENT)
public class GeneratedToolItemModel extends AbstractGeneratedItemModel {

    public static final @NotNull GeneratedToolItemModel MODEL_6_LAYERS = new GeneratedToolItemModel(6);
    public static final @NotNull GeneratedToolItemModel MODEL_12_LAYERS = new GeneratedToolItemModel(12);
    public static final @NotNull GeneratedToolItemModel MODEL_18_LAYERS = new GeneratedToolItemModel(18);
    public static final @NotNull GeneratedToolItemModel MODEL_24_LAYERS = new GeneratedToolItemModel(24);
    public static final @NotNull GeneratedToolItemModel MODEL_30_LAYERS = new GeneratedToolItemModel(30);
    public static final @NotNull GeneratedToolItemModel MODEL_36_LAYERS = new GeneratedToolItemModel(36);

    public GeneratedToolItemModel(final int layerCount) {
        super(layerCount, count -> BlueTools.id("builtin/generated/tool_%d_layers".formatted(count)));
    }

}
