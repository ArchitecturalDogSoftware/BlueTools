package net.architecturaldog.bluetools.content.material;

import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.item.equipment.EquipmentType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;

import java.util.Map;
import java.util.Optional;

public abstract class Material {

    public abstract int maxDurability();

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

    public record WeaponProperties(int damage, float speed, float disableShieldSeconds) {

        public WeaponProperties(int damage, float speed) {
            this(damage, speed, 0.0F);
        }

    }

    public record ToolProperties(Tier tier, float speed, int damagePerBlock) {

        public ToolProperties(Tier tier, float speed) {
            this(tier, speed, 1);
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

}
