package net.architecturaldog.bluetools.content;

import dev.jaxydog.lodestone.api.AutoLoader;
import net.architecturaldog.bluetools.BlueTools;
import net.architecturaldog.bluetools.content.material.Material;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

public final class BlueToolsRegistries extends AutoLoader {

    public static final Registry<Material> MATERIAL = FabricRegistryBuilder
        .createDefaulted(BlueToolsRegistries.Keys.MATERIAL, BlueTools.id("iron"))
        .buildAndRegister();

    @Override
    public Identifier getLoaderId() {
        return BlueTools.id("registries");
    }

    public static final class Keys {

        public static final RegistryKey<Registry<Material>> MATERIAL = RegistryKey.ofRegistry(BlueTools.id("material"));

    }

}
