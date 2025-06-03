package net.architecturaldog.bluetools.content;

import dev.jaxydog.lodestone.api.AutoLoader;
import dev.jaxydog.lodestone.api.IgnoreLoading;
import dev.jaxydog.lodestone.api.LoadingPriority;
import net.architecturaldog.bluetools.BlueTools;
import net.architecturaldog.bluetools.content.block.BlueToolsBlockEntityTypes;
import net.architecturaldog.bluetools.content.block.BlueToolsBlocks;
import net.architecturaldog.bluetools.content.data.BlueToolsResourceReloadListeners;
import net.architecturaldog.bluetools.content.item.BlueToolsItems;
import net.minecraft.util.Identifier;

public final class BlueToolsContent extends AutoLoader {

    @IgnoreLoading({ })
    public static final BlueToolsContent INSTANCE = new BlueToolsContent();

    public static final BlueToolsBlocks BLOCKS = new BlueToolsBlocks();
    public static final BlueToolsBlockEntityTypes BLOCK_ENTITY_TYPES = new BlueToolsBlockEntityTypes();
    public static final BlueToolsItems ITEMS = new BlueToolsItems();
    @LoadingPriority(Integer.MAX_VALUE)
    public static final BlueToolsRegistries REGISTRIES = new BlueToolsRegistries();
    public static final BlueToolsResourceReloadListeners RESOURCE_RELOAD_LISTENERS =
        new BlueToolsResourceReloadListeners();

    @Override
    public Identifier getLoaderId() {
        return BlueTools.id("root");
    }

}
