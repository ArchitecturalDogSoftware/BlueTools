package net.architecturaldog.bluetools.content.component;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.mojang.serialization.Codec;
import com.mojang.serialization.Keyable;
import net.architecturaldog.bluetools.content.material.Material;
import net.architecturaldog.bluetools.content.resource.BlueToolsResources;
import net.architecturaldog.bluetools.content.resource.JsonResourceManager;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.dynamic.Codecs;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Objects;

public record MaterialMapComponent(@NotNull Map<String, JsonResourceManager.Entry<Material>> materials) {

    public static final @NotNull BiMap<Class<? extends Item>, Identifier> PART_TYPES = HashBiMap.create();
    public static final @NotNull Codec<MaterialMapComponent> CODEC = Codec.simpleMap(
        Codecs.NON_EMPTY_STRING,
        BlueToolsResources.MATERIAL.getEntryCodec(),
        Keyable.forStrings(() -> PART_TYPES.values().stream().filter(Objects::nonNull).map(Identifier::toString))
    ).xmap(MaterialMapComponent::new, MaterialMapComponent::materials).codec();

}
