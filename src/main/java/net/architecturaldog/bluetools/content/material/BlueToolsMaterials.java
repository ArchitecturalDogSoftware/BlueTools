package net.architecturaldog.bluetools.content.material;

import dev.jaxydog.lodestone.api.*;
import net.architecturaldog.bluetools.BlueTools;
import net.architecturaldog.bluetools.content.BlueToolsRegistries;
import net.minecraft.item.Item;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.Map;
import java.util.Optional;

public final class BlueToolsMaterials extends AutoLoader {

    public static final RegistrationWrapper<SimpleMaterial> EMPTY = new RegistrationWrapper<>(
        "empty",
        SimpleMaterial.builder().build()
    );

    @Override
    public Identifier getLoaderId() {
        return BlueTools.id("materials");
    }

    public static final class RegistrationWrapper<T extends Material> extends Material
        implements ClientLoaded, CommonLoaded, ServerLoaded, DataGenerating
    {

        private final String path;
        private final T material;

        private RegistrationWrapper(final String path, final T material) {
            this.path = path;
            this.material = material;
        }

        @Override
        public Map<Item, Value> items() {
            return this.material.items();
        }

        @Override
        public Optional<MeltingProperties> meltingProperties() {
            return this.material.meltingProperties();
        }

        @Override
        public Optional<WeaponProperties> weaponProperties() {
            return this.material.weaponProperties();
        }

        @Override
        public Optional<ToolProperties> toolProperties() {
            return this.material.toolProperties();
        }

        @Override
        public Optional<ArmorProperties> armorProperties() {
            return this.material.armorProperties();
        }

        @Override
        public Identifier getLoaderId() {
            return BlueTools.id(this.path);
        }

        @Override
        public void loadClient() {
            if (this.material instanceof final ClientLoaded clientLoaded) {
                clientLoaded.loadClient();
            }
        }

        @Override
        public void loadCommon() {
            Registry.register(BlueToolsRegistries.MATERIAL, this.getLoaderId(), this);

            if (this.material instanceof final CommonLoaded commonLoaded) {
                commonLoaded.loadCommon();
            }
        }

        @Override
        public void loadServer() {
            if (this.material instanceof final ServerLoaded serverLoaded) {
                serverLoaded.loadServer();
            }
        }

        @Override
        public void generate() {
            if (this.material instanceof final DataGenerating dataGenerating) {
                dataGenerating.generate();
            }
        }

    }

}
