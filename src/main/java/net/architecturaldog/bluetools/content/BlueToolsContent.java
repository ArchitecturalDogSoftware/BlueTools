package net.architecturaldog.bluetools.content;

import dev.jaxydog.lodestone.api.AutoLoader;
import dev.jaxydog.lodestone.api.IgnoreLoading;
import dev.jaxydog.lodestone.api.LoadingPriority;
import net.architecturaldog.bluetools.BlueTools;
import net.architecturaldog.bluetools.content.block.BlueToolsBlockEntityTypes;
import net.architecturaldog.bluetools.content.block.BlueToolsBlocks;
import net.architecturaldog.bluetools.content.component.BlueToolsComponentTypes;
import net.architecturaldog.bluetools.content.item.BlueToolsItemGroups;
import net.architecturaldog.bluetools.content.item.BlueToolsItems;
import net.architecturaldog.bluetools.content.material.BlueToolsMaterialTypes;
import net.architecturaldog.bluetools.content.material.property.BlueToolsMaterialPropertyTypes;
import net.architecturaldog.bluetools.content.part.BlueToolsPartTypes;
import net.architecturaldog.bluetools.content.part.property.BlueToolsPartPropertyTypes;
import net.architecturaldog.bluetools.content.resource.BlueToolsResources;
import net.architecturaldog.bluetools.content.screen.BlueToolsScreenHandlerTypes;
import net.architecturaldog.bluetools.content.tool.BlueToolsToolTypes;
import net.architecturaldog.bluetools.content.tool.property.BlueToolsToolPropertyTypes;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

public final class BlueToolsContent extends AutoLoader {

    @IgnoreLoading({ })
    public static final @NotNull BlueToolsContent INSTANCE = new BlueToolsContent();

    public static final @NotNull BlueToolsBlocks BLOCKS = new BlueToolsBlocks();
    public static final @NotNull BlueToolsBlockEntityTypes BLOCK_ENTITY_TYPES = new BlueToolsBlockEntityTypes();
    public static final @NotNull BlueToolsComponentTypes COMPONENT_TYPES = new BlueToolsComponentTypes();
    public static final @NotNull BlueToolsItemGroups ITEM_GROUPS = new BlueToolsItemGroups();
    public static final @NotNull BlueToolsItems ITEMS = new BlueToolsItems();
    public static final @NotNull BlueToolsMaterialPropertyTypes MATERIAL_PROPERTY_TYPES = new BlueToolsMaterialPropertyTypes();
    public static final @NotNull BlueToolsMaterialTypes MATERIAL_TYPES = new BlueToolsMaterialTypes();
    public static final @NotNull BlueToolsPartPropertyTypes PART_PROPERTY_TYPES = new BlueToolsPartPropertyTypes();
    public static final @NotNull BlueToolsPartTypes PART_TYPES = new BlueToolsPartTypes();
    @LoadingPriority(Integer.MAX_VALUE)
    public static final @NotNull BlueToolsRegistries REGISTRIES = new BlueToolsRegistries();
    public static final @NotNull BlueToolsResources RESOURCES = new BlueToolsResources();
    public static final @NotNull BlueToolsScreenHandlerTypes SCREEN_HANDLER_TYPES = new BlueToolsScreenHandlerTypes();
    public static final @NotNull BlueToolsToolPropertyTypes TOOL_PROPERTY_TYPES = new BlueToolsToolPropertyTypes();
    public static final @NotNull BlueToolsToolTypes TOOL_TYPES = new BlueToolsToolTypes();

    @Override
    public @NotNull Identifier getLoaderId() {
        return BlueTools.id("root");
    }

}
