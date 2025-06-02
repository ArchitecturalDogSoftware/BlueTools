package net.architecturaldog.bluetools.content.material;

import com.google.common.collect.ImmutableMap;
import dev.jaxydog.lodestone.api.CommonLoaded;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.architecturaldog.bluetools.BlueTools;
import net.architecturaldog.bluetools.content.BlueToolsRegistries;
import net.minecraft.item.Item;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Optional;

public abstract class BlueToolsMaterial extends Material implements CommonLoaded {

    private final String path;

    public BlueToolsMaterial(final String path) {
        this.path = path;
    }

    public static Builder builder(final String path) {
        return new Builder(path);
    }

    @Override
    public Identifier getLoaderId() {
        return BlueTools.id(this.path);
    }

    @Override
    public void loadCommon() {
        Registry.register(BlueToolsRegistries.MATERIAL, this.getLoaderId(), this);
    }

    public static final class Builder {

        private final String path;
        private final Map<Item, Value> items = new Object2ObjectOpenHashMap<>();
        private @Nullable MeltingProperties meltingProperties;
        private @Nullable WeaponProperties weaponProperties;
        private @Nullable ToolProperties toolProperties;
        private @Nullable ArmorProperties armorProperties;

        private Builder(final String path) {
            this.path = path;
        }

        public Builder item(final Item item, final long fluidValue) {
            this.items.put(item, new Value(fluidValue));

            return this;
        }

        public Builder melting(final MeltingProperties properties) {
            this.meltingProperties = properties;

            return this;
        }

        public Builder weapon(final WeaponProperties properties) {
            this.weaponProperties = properties;

            return this;
        }

        public Builder tool(final ToolProperties properties) {
            this.toolProperties = properties;

            return this;
        }

        public Builder armor(final ArmorProperties properties) {
            this.armorProperties = properties;

            return this;
        }

        public BlueToolsMaterial build() {
            return new BlueToolsMaterial(this.path) {

                @Override
                public Map<Item, Value> items() {
                    return ImmutableMap.copyOf(Builder.this.items);
                }

                @Override
                public Optional<MeltingProperties> meltingProperties() {
                    return Optional.ofNullable(Builder.this.meltingProperties);
                }

                @Override
                public Optional<WeaponProperties> weaponProperties() {
                    return Optional.ofNullable(Builder.this.weaponProperties);
                }

                @Override
                public Optional<ToolProperties> toolProperties() {
                    return Optional.ofNullable(Builder.this.toolProperties);
                }

                @Override
                public Optional<ArmorProperties> armorProperties() {
                    return Optional.ofNullable(Builder.this.armorProperties);
                }

            };
        }

    }

}
