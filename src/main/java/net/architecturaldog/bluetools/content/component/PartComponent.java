package net.architecturaldog.bluetools.content.component;

import com.mojang.serialization.Codec;
import net.architecturaldog.bluetools.content.part.Part;
import net.architecturaldog.bluetools.content.resource.BlueToolsResources;
import net.architecturaldog.bluetools.content.resource.JsonResourceManager;
import org.jetbrains.annotations.NotNull;

public record PartComponent(@NotNull JsonResourceManager.Entry<Part> partEntry) {

    public static final @NotNull Codec<PartComponent> CODEC =
        BlueToolsResources.PART.getEntryCodec().xmap(PartComponent::new, PartComponent::partEntry);

}
