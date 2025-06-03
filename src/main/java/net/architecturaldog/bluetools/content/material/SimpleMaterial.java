package net.architecturaldog.bluetools.content.material;

import net.minecraft.item.Item;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Optional;

public class SimpleMaterial extends Material {

    private final Map<Item, Value> items;
    private final @Nullable MeltingProperties meltingProperties;
    private final @Nullable WeaponProperties weaponProperties;
    private final @Nullable ToolProperties toolProperties;
    private final @Nullable ArmorProperties armorProperties;

    public SimpleMaterial(
        final Map<Item, Value> items,
        final @Nullable MeltingProperties meltingProperties,
        final @Nullable WeaponProperties weaponProperties,
        final @Nullable ToolProperties toolProperties,
        final @Nullable ArmorProperties armorProperties
    )
    {
        this.items = items;
        this.meltingProperties = meltingProperties;
        this.weaponProperties = weaponProperties;
        this.toolProperties = toolProperties;
        this.armorProperties = armorProperties;
    }

    public static Builder<SimpleMaterial> builder() {
        return new Builder<>(SimpleMaterial::new);
    }

    @Override
    public Map<Item, Value> items() {
        return this.items;
    }

    @Override
    public Optional<MeltingProperties> meltingProperties() {
        return Optional.ofNullable(this.meltingProperties);
    }

    @Override
    public Optional<WeaponProperties> weaponProperties() {
        return Optional.ofNullable(this.weaponProperties);
    }

    @Override
    public Optional<ToolProperties> toolProperties() {
        return Optional.ofNullable(this.toolProperties);
    }

    @Override
    public Optional<ArmorProperties> armorProperties() {
        return Optional.ofNullable(this.armorProperties);
    }

}
