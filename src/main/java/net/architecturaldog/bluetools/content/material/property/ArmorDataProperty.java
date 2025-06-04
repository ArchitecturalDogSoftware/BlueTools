package net.architecturaldog.bluetools.content.material.property;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.item.equipment.EquipmentType;
import net.minecraft.registry.Registries;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.StringIdentifiable;

import java.util.Map;

public record ArmorDataProperty(
    Map<EquipmentType, Integer> defense,
    float toughness,
    float knockbackResistance,
    SoundEvent equipSound
) implements MaterialProperty
{

    public static final MapCodec<ArmorDataProperty> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        Codec
            .simpleMap(
                EquipmentType.CODEC,
                Codec.intRange(0, Integer.MAX_VALUE),
                StringIdentifiable.toKeyable(EquipmentType.values())
            )
            .fieldOf("defense")
            .forGetter(ArmorDataProperty::defense),
        Codec
            .floatRange(0.0F, Float.MAX_VALUE)
            .optionalFieldOf("toughness", 0.0F)
            .forGetter(ArmorDataProperty::toughness),
        Codec
            .floatRange(0.0F, Float.MAX_VALUE)
            .optionalFieldOf("knockback_resistance", 0.0F)
            .forGetter(ArmorDataProperty::knockbackResistance),
        Registries.SOUND_EVENT
            .getCodec()
            .optionalFieldOf("equip_sound", SoundEvents.ITEM_ARMOR_EQUIP_GENERIC.value())
            .forGetter(ArmorDataProperty::equipSound)
    ).apply(instance, ArmorDataProperty::new));

    @Override
    public MaterialPropertyType<? extends MaterialProperty> getType() {
        return null;
    }

}
