package net.architecturaldog.bluetools.content.component;

import com.mojang.serialization.Codec;
import net.architecturaldog.bluetools.content.material.Material;
import net.architecturaldog.bluetools.content.resource.BlueToolsResources;
import net.architecturaldog.bluetools.content.resource.JsonResourceManager;
import org.jetbrains.annotations.NotNull;

public record MaterialComponent(@NotNull JsonResourceManager.Entry<Material> materialEntry) {

    public static final @NotNull Codec<MaterialComponent> CODEC =
        BlueToolsResources.MATERIAL.getEntryCodec().xmap(MaterialComponent::new, MaterialComponent::materialEntry);

}
