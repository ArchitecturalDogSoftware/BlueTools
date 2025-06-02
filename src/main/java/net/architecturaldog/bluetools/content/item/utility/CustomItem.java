package net.architecturaldog.bluetools.content.item.utility;

import dev.jaxydog.lodestone.api.CommonLoaded;
import net.minecraft.item.ItemConvertible;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public interface CustomItem extends CommonLoaded, ItemConvertible {

    default void loadCommon() {
        Registry.register(Registries.ITEM, this.getLoaderId(), this.asItem());
    }

}
