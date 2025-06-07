package net.architecturaldog.bluetools.content.component;

import com.mojang.serialization.Codec;
import net.architecturaldog.bluetools.content.material.Material;
import net.architecturaldog.bluetools.content.resource.BlueToolsResources;

public record MaterialComponent(Material material) {

    public static final Codec<MaterialComponent> CODEC =
        BlueToolsResources.MATERIAL.getCodec().xmap(MaterialComponent::new, MaterialComponent::material);

}
