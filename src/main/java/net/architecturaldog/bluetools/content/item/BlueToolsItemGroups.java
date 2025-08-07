package net.architecturaldog.bluetools.content.item;

import dev.jaxydog.lodestone.api.AutoLoaded;
import dev.jaxydog.lodestone.api.AutoLoader;
import net.architecturaldog.bluetools.BlueTools;
import net.architecturaldog.bluetools.utility.RegistryLoaded;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

public final class BlueToolsItemGroups extends AutoLoader {

    public static final @NotNull AutoLoaded<ItemGroup> DEFAULT = BlueToolsItemGroups.create(
        "default",
        FabricItemGroup
            .builder()
            .displayName(Text.translatable(BlueTools.id("default").toTranslationKey("itemGroup")))
            .icon(BlueToolsItems.FORGE_INTERFACE.getValue()::getDefaultStack)
            .build()
    );
    public static final @NotNull AutoLoaded<ItemGroup> PARTS = BlueToolsItemGroups.create(
        "parts",
        FabricItemGroup
            .builder()
            .displayName(Text.translatable(BlueTools.id("parts").toTranslationKey("itemGroup")))
            .icon(() -> BlueToolsItems.PART.getValue().getDefaultStack())
            .build()
    );

    public static @NotNull RegistryKey<ItemGroup> registryKey(final @NotNull AutoLoaded<? extends ItemGroup> loaded) {
        return RegistryKey.of(RegistryKeys.ITEM_GROUP, loaded.getLoaderId());
    }

    private static <G extends ItemGroup> @NotNull AutoLoaded<G> create(
        final @NotNull String path,
        final @NotNull G itemGroup
    )
    {
        return BlueToolsItemGroups.create(BlueTools.id(path), itemGroup);
    }

    private static <G extends ItemGroup> @NotNull AutoLoaded<G> create(
        final @NotNull Identifier identifier,
        final @NotNull G itemGroup
    )
    {
        return new RegistryLoaded<>(identifier, Registries.ITEM_GROUP, itemGroup);
    }

    @Override
    public Identifier getLoaderId() {
        return BlueTools.id("item_groups");
    }

}
