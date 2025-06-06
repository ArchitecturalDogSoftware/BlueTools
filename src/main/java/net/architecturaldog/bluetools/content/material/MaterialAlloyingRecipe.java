package net.architecturaldog.bluetools.content.material;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.architecturaldog.bluetools.content.BlueToolsResources;

import java.util.List;

public record MaterialAlloyingRecipe(
    List<Ingredient> input,
    List<Ingredient> output,
    MaterialTemperature requiredTemperature
)
{

    public static final MapCodec<MaterialAlloyingRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        Codec
            .list(Ingredient.CODEC.codec(), 2, Integer.MAX_VALUE)
            .fieldOf("input")
            .forGetter(MaterialAlloyingRecipe::input),
        Codec
            .list(Ingredient.CODEC.codec(), 1, Integer.MAX_VALUE)
            .fieldOf("output")
            .forGetter(MaterialAlloyingRecipe::output),
        MaterialTemperature.CODEC.fieldOf("required_temperature").forGetter(MaterialAlloyingRecipe::requiredTemperature)
    ).apply(instance, MaterialAlloyingRecipe::new));

    public record Ingredient(Material material, MaterialValue value) {

        public static final MapCodec<Ingredient> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            BlueToolsResources.MATERIAL.getCodec().fieldOf("material").forGetter(Ingredient::material),
            MaterialValue.CODEC.codec().fieldOf("value").forGetter(Ingredient::value)
        ).apply(instance, Ingredient::new));

    }

}
