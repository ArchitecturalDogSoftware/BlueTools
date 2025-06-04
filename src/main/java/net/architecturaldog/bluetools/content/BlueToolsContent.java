package net.architecturaldog.bluetools.content;

import dev.jaxydog.lodestone.api.AutoLoader;
import dev.jaxydog.lodestone.api.IgnoreLoading;
import dev.jaxydog.lodestone.api.LoadingPriority;
import net.architecturaldog.bluetools.BlueTools;
import net.architecturaldog.bluetools.content.block.BlueToolsBlockEntityTypes;
import net.architecturaldog.bluetools.content.block.BlueToolsBlocks;
import net.architecturaldog.bluetools.content.component.BlueToolsComponentTypes;
import net.architecturaldog.bluetools.content.item.BlueToolsItems;
import net.architecturaldog.bluetools.content.material.BlueToolsMaterialPropertyTypes;
import net.architecturaldog.bluetools.content.resource.BlueToolsResourceReloadListeners;
import net.minecraft.util.Identifier;

public final class BlueToolsContent extends AutoLoader {

    @IgnoreLoading({ })
    public static final BlueToolsContent INSTANCE = new BlueToolsContent();

    public static final BlueToolsBlocks BLOCKS = new BlueToolsBlocks();
    public static final BlueToolsBlockEntityTypes BLOCK_ENTITY_TYPES = new BlueToolsBlockEntityTypes();
    public static final BlueToolsComponentTypes COMPONENT_TYPES = new BlueToolsComponentTypes();
    public static final BlueToolsItems ITEMS = new BlueToolsItems();
    public static final BlueToolsMaterialPropertyTypes MATERIAL_PROPERTY_TYPES = new BlueToolsMaterialPropertyTypes();
    @LoadingPriority(Integer.MAX_VALUE)
    public static final BlueToolsRegistries REGISTRIES = new BlueToolsRegistries();
    public static final BlueToolsResourceReloadListeners RESOURCE_RELOAD_LISTENERS =
        new BlueToolsResourceReloadListeners();

    @Override
    public Identifier getLoaderId() {
        return BlueTools.id("root");
    }

}
