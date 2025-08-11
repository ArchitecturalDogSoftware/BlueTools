package net.architecturaldog.bluetools.content.resource;

import dev.jaxydog.lodestone.api.AutoLoader;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.architecturaldog.bluetools.BlueTools;
import net.architecturaldog.bluetools.content.BlueToolsRegistries;
import net.architecturaldog.bluetools.content.material.Material;
import net.architecturaldog.bluetools.content.material.MaterialIngredient;
import net.architecturaldog.bluetools.content.material.MiningLevel;
import net.architecturaldog.bluetools.content.part.Part;
import net.architecturaldog.bluetools.content.tool.Tool;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnknownNullability;

import java.util.List;
import java.util.Map;

public final class BlueToolsResources extends AutoLoader {

    public static final @NotNull SimpleJsonResourceManager<MiningLevel> MINING_LEVEL = new SimpleJsonResourceManager<>(
        "mining_level",
        BlueToolsRegistries.Keys.MINING_LEVEL,
        MiningLevel.CODEC.codec()
    );

    public static final @NotNull SimpleJsonResourceManager<Material> MATERIAL = new SimpleJsonResourceManager<>(
        "material",
        BlueToolsRegistries.Keys.MATERIAL,
        Material.CODEC,
        List.of(BlueToolsResources.MINING_LEVEL.getFabricId())
    );

    public static final @NotNull SimpleJsonResourceManager<MaterialIngredient> MATERIAL_INGREDIENT =
        new SimpleJsonResourceManager<>(
            "material_ingredient",
            BlueToolsRegistries.Keys.MATERIAL_INGREDIENT,
            MaterialIngredient.CODEC.codec(),
            List.of(BlueToolsResources.MATERIAL.getFabricId())
        )
        {

            private @UnknownNullability Map<MaterialIngredient.Ingredient, RegistryKey<MaterialIngredient>> ingredientCache;

            @Override
            protected void prepareVerification() {
                this.ingredientCache = new Object2ObjectOpenHashMap<>(this.getEntries().size());
            }

            @Override
            protected void cleanupVerification() {
                this.ingredientCache = null;
            }

            @Override
            protected boolean verifyEntry(final @NotNull Entry<MaterialIngredient> entry) {
                if (!this.ingredientCache.containsKey(entry.value().ingredient())) {
                    this.ingredientCache.put(entry.value().ingredient(), entry.key());

                    return super.verifyEntry(entry);
                }

                BlueTools.LOGGER.error(
                    "Duplicate ingredients for JSON manager '{}': {}, {}",
                    this.getName(),
                    this.ingredientCache.get(entry.value().ingredient()),
                    entry.key()
                );

                return false;
            }

        };

    public static final @NotNull SimpleJsonResourceManager<Part> PART = new SimpleJsonResourceManager<>(
        "part",
        BlueToolsRegistries.Keys.PART,
        Part.CODEC,
        List.of(BlueToolsResources.MATERIAL.getFabricId())
    );

    public static final @NotNull SimpleJsonResourceManager<Tool> TOOL = new SimpleJsonResourceManager<>(
        "tool",
        BlueToolsRegistries.Keys.TOOL,
        Tool.CODEC,
        List.of(BlueToolsResources.MATERIAL.getFabricId(), BlueToolsResources.PART.getFabricId())
    );

    @Override
    public @NotNull Identifier getLoaderId() {
        return BlueTools.id("resources");
    }

}
