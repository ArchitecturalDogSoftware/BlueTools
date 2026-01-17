package net.architecturaldog.bluetools.content.resource;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.jetbrains.annotations.Nullable;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.architecturaldog.bluetools.content.material.MaterialIngredient;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

public class MaterialIngredientResourceManager extends SimpleJsonResourceManager<MaterialIngredient> {

    private @Nullable Map<MaterialIngredient.Ingredient, RegistryKey<MaterialIngredient>> ingredientCache = null;

    public MaterialIngredientResourceManager(
        final String name,
        final RegistryKey<Registry<MaterialIngredient>> registryKey,
        final List<Identifier> loaderDependencies
    )
    {
        super(name, registryKey, MaterialIngredient.CODEC.codec(), loaderDependencies);
    }

    public MaterialIngredientResourceManager(
        final String name,
        final RegistryKey<Registry<MaterialIngredient>> registryKey
    )
    {
        super(name, registryKey, MaterialIngredient.CODEC.codec());
    }

    public MaterialIngredientResourceManager(
        final RegistryKey<Registry<MaterialIngredient>> registryKey,
        final List<Identifier> loaderDependencies
    )
    {
        super(registryKey, MaterialIngredient.CODEC.codec(), loaderDependencies);
    }

    public MaterialIngredientResourceManager(final RegistryKey<Registry<MaterialIngredient>> registryKey) {
        super(registryKey, MaterialIngredient.CODEC.codec());
    }

    @Override
    protected void prepareVerification() {
        this.ingredientCache = new Object2ObjectOpenHashMap<>(this.getEntries().size());
    }

    @Override
    protected boolean verifyEntry(final Entry<MaterialIngredient> entry) {
        if (Objects.isNull(this.ingredientCache)) {
            throw new IllegalStateException("The ingredient cache must be initialized");
        }

        if (this.ingredientCache.containsKey(entry.value().ingredient())) {
            JsonResourceManager.LOGGER
                .error(
                    "Duplicate ingredients for JSON manager '{}': {}, {}",
                    this.getName(),
                    this.ingredientCache.get(entry.value().ingredient()),
                    entry.key()
                );

            return false;
        }

        this.ingredientCache.put(entry.value().ingredient(), entry.key());

        return super.verifyEntry(entry);
    }

    @Override
    protected void cleanupVerification() {
        this.ingredientCache = null;
    }

}
