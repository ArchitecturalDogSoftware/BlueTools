package net.architecturaldog.bluetools.content.material;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.architecturaldog.bluetools.content.material.property.BlueToolsMaterialPropertyTypes;
import net.architecturaldog.bluetools.content.resource.BlueToolsResources;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.StringIdentifiable;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public record MaterialIngredient(
    @NotNull Material material,
    @NotNull MaterialIngredient.ItemType itemType,
    @NotNull Ingredient ingredient,
    @NotNull MaterialValue value
)
{

    public static final MapCodec<MaterialIngredient> CODEC = RecordCodecBuilder
        .<MaterialIngredient>mapCodec(instance -> instance
            .group(
                BlueToolsResources.MATERIAL.getCodec().fieldOf("material").forGetter(MaterialIngredient::material),
                ItemType.CODEC.fieldOf("item_type").forGetter(MaterialIngredient::itemType),
                Ingredient.CODEC.fieldOf("ingredient").forGetter(MaterialIngredient::ingredient),
                MaterialValue.POSITIVE_CODEC.fieldOf("value").forGetter(MaterialIngredient::value)
            )
            .apply(instance, MaterialIngredient::new))
        .validate(MaterialIngredient::validateCodecResult);

    private @NotNull DataResult<MaterialIngredient> validateCodecResult() {
        final boolean hasFluid = this.material().hasProperty(BlueToolsMaterialPropertyTypes.FLUID.getValue());

        return !this.itemType().needsFluid() || hasFluid
            ? DataResult.success(this)
            : DataResult.error(() -> "Assigned material for ingredient " + this + " does not have an associated fluid");
    }

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

    public enum ItemType implements StringIdentifiable {

        CRAFT_ITEM("craft_item", false),
        FROM_FLUID("from_fluid", true),
        INTO_FLUID("into_fluid", true),
        SWAP_FLUID("swap_fluid", true);

        public static final @NotNull EnumCodec<ItemType> CODEC = StringIdentifiable.createCodec(ItemType::values);

        private final @NotNull String key;
        private final boolean needsFluid;

        ItemType(final @NotNull String key, final boolean needsFluid) {
            this.key = key;
            this.needsFluid = needsFluid;
        }

        public boolean needsFluid() {
            return this.needsFluid;
        }

        @Override
        public String asString() {
            return this.key;
        }

    }

}
