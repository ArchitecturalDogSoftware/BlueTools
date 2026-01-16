package net.architecturaldog.bluetools.content.component;

import com.mojang.serialization.Codec;

import net.architecturaldog.bluetools.content.material.Material;
import net.architecturaldog.bluetools.content.resource.BlueToolsResources;
import net.architecturaldog.bluetools.content.resource.JsonResourceManager;

public record MaterialComponent(JsonResourceManager.Entry<Material> materialEntry) {

    public static final Codec<MaterialComponent> CODEC = BlueToolsResources.MATERIAL
        .getEntryCodec()
        .xmap(MaterialComponent::new, MaterialComponent::materialEntry);

}
