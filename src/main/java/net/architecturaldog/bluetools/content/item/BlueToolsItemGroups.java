package net.architecturaldog.bluetools.content.item;

import java.util.function.UnaryOperator;

import dev.jaxydog.lodestone.api.AutoLoaded;
import dev.jaxydog.lodestone.api.AutoLoader;
import dev.jaxydog.lodestone.api.CommonLoaded;
import net.architecturaldog.bluetools.utility.BlueToolsHelper;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public final class BlueToolsItemGroups extends AutoLoader {

    public static final AutoLoaded<ItemGroup> DEFAULT = BlueToolsItemGroups.createVanilla("default", builder -> {
        return builder.icon(BlueToolsItems.FORGE_INTERFACE.getValue()::getDefaultStack);
    });
    public static final AutoLoaded<ItemGroup> PARTS = BlueToolsItemGroups.createVanilla("parts", builder -> {
        return builder.icon(() -> BlueToolsItems.PART.getValue().getDefaultStack());
    });
    public static final AutoLoaded<ItemGroup> TOOLS = BlueToolsItemGroups.createVanilla("tools", builder -> {
        return builder.icon(() -> BlueToolsItems.PART.getValue().getDefaultStack());
    });

    public static RegistryKey<ItemGroup> registryKey(final AutoLoaded<? extends ItemGroup> loaded) {
        return RegistryKey.of(RegistryKeys.ITEM_GROUP, loaded.getLoaderId());
    }

    private static AutoLoaded<ItemGroup> createVanilla(
        final String path,
        final UnaryOperator<ItemGroup.Builder> builder
    )
    {
        return BlueToolsItemGroups.createVanilla(BlueToolsHelper.createIdentifier(path), builder);
    }

    private static AutoLoaded<ItemGroup> createVanilla(
        final Identifier identifier,
        final UnaryOperator<ItemGroup.Builder> builder
    )
    {
        final Text displayName = Text.translatable(identifier.toTranslationKey("itemGroup"));
        final ItemGroup.Builder itemGroup = FabricItemGroup.builder().displayName(displayName);

        return BlueToolsItemGroups.create(identifier, builder.apply(itemGroup).build());
    }

    private static <G extends ItemGroup> AutoLoaded<G> create(
        final String path,
        final G itemGroup
    )
    {
        return BlueToolsItemGroups.create(BlueToolsHelper.createIdentifier(path), itemGroup);
    }

    private static <G extends ItemGroup> AutoLoaded<G> create(
        final Identifier identifier,
        final G itemGroup
    )
    {
        return new AutoLoaded<>(identifier, itemGroup).on(CommonLoaded.class, self -> {
            Registry.register(Registries.ITEM_GROUP, self.getLoaderId(), self.getValue());
        });
    }

    @Override
    public Identifier getLoaderId() {
        return BlueToolsHelper.createIdentifier("item_groups");
    }

}
