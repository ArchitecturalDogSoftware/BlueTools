package net.architecturaldog.bluetools.content.material;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.item.equipment.EquipmentType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Optional;

public abstract class Material {

    public abstract Map<Item, Value> items();

    public abstract Optional<MeltingProperties> meltingProperties();

    public abstract Optional<WeaponProperties> weaponProperties();

    public abstract Optional<ToolProperties> toolProperties();

    public abstract Optional<ArmorProperties> armorProperties();

    public record Value(long fluidValue) {

        public Value {
            assert this.fluidValue() > 0;
        }

    }

    public record MeltingProperties(RegistryKey<Fluid> fluidKey, int temperature) {

    }

    public record WeaponProperties(int durability, int damage, float speed, float disableShieldSeconds) {

        public WeaponProperties(int durability, int damage, float speed) {
            this(durability, damage, speed, 0.0F);
        }

    }

    public record ToolProperties(int durability, Tier tier, float speed, int damagePerBlock) {

        public ToolProperties(int durability, Tier tier, float speed) {
            this(durability, tier, speed, 1);
        }

        public enum Tier {

            NONE,
            WOOD,
            STONE,
            IRON,
            DIAMOND,

        }

    }

    public record ArmorProperties(
        int durability,
        float toughness,
        float knockbackResistance,
        Map<EquipmentType, Integer> defense,
        RegistryEntry<SoundEvent> equipSoundEvent
    )
    {

        public ArmorProperties(
            int durability,
            float toughness,
            float knockbackResistance,
            Map<EquipmentType, Integer> defense
        )
        {
            this(durability, toughness, knockbackResistance, defense, SoundEvents.ITEM_ARMOR_EQUIP_GENERIC);
        }

    }

    @FunctionalInterface
    public interface Factory<T extends Material> {

        T create(
            final Map<Item, Value> items,
            final @Nullable MeltingProperties meltingProperties,
            final @Nullable WeaponProperties weaponProperties,
            final @Nullable ToolProperties toolProperties,
            final @Nullable ArmorProperties armorProperties
        );

    }

    public static class Builder<T extends Material> {

        private final Factory<T> factory;
        private final Map<Item, Value> items = new Object2ObjectOpenHashMap<>();
        private @Nullable MeltingProperties meltingProperties;
        private @Nullable WeaponProperties weaponProperties;
        private @Nullable ToolProperties toolProperties;
        private @Nullable ArmorProperties armorProperties;

        public Builder(final Factory<T> factory) {
            this.factory = factory;
        }

        public Builder<T> item(final Item item, final long fluidValue) {
            this.items.put(item, new Value(fluidValue));

            return this;
        }

        public Builder<T> melting(final MeltingProperties properties) {
            this.meltingProperties = properties;

            return this;
        }

        public Builder<T> weapon(final WeaponProperties properties) {
            this.weaponProperties = properties;

            return this;
        }

        public Builder<T> tool(final ToolProperties properties) {
            this.toolProperties = properties;

            return this;
        }

        public Builder<T> armor(final ArmorProperties properties) {
            this.armorProperties = properties;

            return this;
        }

        public T build() {
            return this.factory.create(
                this.items,
                this.meltingProperties,
                this.weaponProperties,
                this.toolProperties,
                this.armorProperties
            );
        }

    }

}
