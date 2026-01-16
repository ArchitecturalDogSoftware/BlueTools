package net.architecturaldog.bluetools.content.material.property;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.architecturaldog.bluetools.utility.Color;
import net.minecraft.util.StringIdentifiable;

public record ColorProperty(
    Color highlight,
    Color base,
    Color shadow,
    Color borderHighlight,
    Color border,
    Color borderShadow
) implements MaterialProperty
{

    public static final MapCodec<ColorProperty> CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance
            .group(
                Color.CODEC.fieldOf("highlight").forGetter(ColorProperty::highlight),
                Color.CODEC.fieldOf("base").forGetter(ColorProperty::base),
                Color.CODEC.fieldOf("shadow").forGetter(ColorProperty::shadow),
                Color.CODEC.fieldOf("border_highlight").forGetter(ColorProperty::borderHighlight),
                Color.CODEC.fieldOf("border").forGetter(ColorProperty::border),
                Color.CODEC.fieldOf("border_shadow").forGetter(ColorProperty::borderShadow)
            )
            .apply(instance, ColorProperty::new);
    });

    public Color getColor(final PaletteColor paletteColor) {
        return switch (paletteColor) {
            case HIGHLIGHT -> this.highlight();
            case BASE -> this.base();
            case SHADOW -> this.shadow();
            case BORDER_HIGHLIGHT -> this.borderHighlight();
            case BORDER -> this.border();
            case BORDER_SHADOW -> this.borderShadow();
        };
    }

    @Override
    public MaterialPropertyType<ColorProperty> getType() {
        return BlueToolsMaterialPropertyTypes.COLOR.getValue();
    }

    public enum PaletteColor implements StringIdentifiable {

        HIGHLIGHT("highlight"),
        BASE("base"),
        SHADOW("shadow"),
        BORDER_HIGHLIGHT("border_highlight"),
        BORDER("border"),
        BORDER_SHADOW("border_shadow");

        public static final Codec<PaletteColor> CODEC = StringIdentifiable.createCodec(PaletteColor::values);

        private final String key;

        PaletteColor(final String key) {
            this.key = key;
        }

        @Override
        public String asString() {
            return this.key;
        }

    }

}
