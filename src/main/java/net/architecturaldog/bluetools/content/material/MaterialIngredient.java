package net.architecturaldog.bluetools.content.material;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.architecturaldog.bluetools.content.resource.BlueToolsResources;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public record MaterialIngredient(
    @NotNull Material material,
    @NotNull Ingredient ingredient,
    @NotNull MaterialValue value
)
{

    public static final MapCodec<MaterialIngredient> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        BlueToolsResources.MATERIAL.getCodec().fieldOf("material").forGetter(MaterialIngredient::material),
        Ingredient.CODEC.fieldOf("ingredient").forGetter(MaterialIngredient::ingredient),
        MaterialValue.CODEC.validate(value -> value.asDroplets() > 0
            ? DataResult.success(value)
            : DataResult.error(() -> "Value " + value + " must be non-zero")
        ).fieldOf("value").forGetter(MaterialIngredient::value)
    ).apply(instance, MaterialIngredient::new));

    public sealed interface Ingredient permits ItemIngredient, TagIngredient {

        @NotNull MapCodec<Ingredient> CODEC = Codec.mapEither(ItemIngredient.CODEC, TagIngredient.CODEC).xmap(
            either -> either.map(Function.identity(), Function.identity()),
            ingredient -> ingredient instanceof ItemIngredient
                ? Either.left((ItemIngredient) ingredient)
                : Either.right((TagIngredient) ingredient)
        );

    }

    public record ItemIngredient(@NotNull Item item) implements Ingredient {

        public static final @NotNull MapCodec<ItemIngredient> CODEC = RecordCodecBuilder.mapCodec(instance -> instance
            .group(Registries.ITEM.getCodec().fieldOf("item").forGetter(ItemIngredient::item))
            .apply(instance, ItemIngredient::new));

    }

    public record TagIngredient(@NotNull TagKey<Item> tagKey) implements Ingredient {

        public static final @NotNull MapCodec<TagIngredient> CODEC = RecordCodecBuilder.mapCodec(instance -> instance
            .group(TagKey.codec(RegistryKeys.ITEM).fieldOf("tag").forGetter(TagIngredient::tagKey))
            .apply(instance, TagIngredient::new));

    }

}
