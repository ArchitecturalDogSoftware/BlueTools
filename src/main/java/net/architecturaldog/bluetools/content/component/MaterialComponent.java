package net.architecturaldog.bluetools.content.component;

import com.mojang.serialization.Codec;
import net.architecturaldog.bluetools.content.material.Material;
import net.architecturaldog.bluetools.content.resource.BlueToolsResources;
import org.jetbrains.annotations.NotNull;

public record MaterialComponent(@NotNull Material material) {

    public static final @NotNull Codec<MaterialComponent> CODEC =
        BlueToolsResources.MATERIAL.getCodec().xmap(MaterialComponent::new, MaterialComponent::material);

}
