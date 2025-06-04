package net.architecturaldog.bluetools.content.item;

import dev.jaxydog.lodestone.api.AutoLoader;
import net.architecturaldog.bluetools.BlueTools;
import net.architecturaldog.bluetools.utility.RegistryLoaded;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

public final class BlueToolsItems extends AutoLoader {

    private static <T extends Item> RegistryLoaded<T> create(final String path, final T value) {
        return new RegistryLoaded<>(path, Registries.ITEM, value);
    }

    @Override
    public Identifier getLoaderId() {
        return BlueTools.id("items");
    }

}
