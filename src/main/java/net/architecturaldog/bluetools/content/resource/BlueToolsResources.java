package net.architecturaldog.bluetools.content.resource;

import dev.jaxydog.lodestone.api.AutoLoader;
import net.architecturaldog.bluetools.BlueTools;
import net.architecturaldog.bluetools.content.material.Material;
import net.architecturaldog.bluetools.content.material.MaterialAlloyingRecipe;
import net.architecturaldog.bluetools.content.material.MaterialMiningLevel;
import net.minecraft.util.Identifier;

public final class BlueToolsResources extends AutoLoader {

    public static final MaterialMiningLevel.Manager MATERIAL_MINING_LEVEL = new MaterialMiningLevel.Manager();
    public static final Material.Manager MATERIAL = new Material.Manager();
    public static final MaterialAlloyingRecipe.Manager MATERIAL_ALLOYING_RECIPE = new MaterialAlloyingRecipe.Manager();

    @Override
    public Identifier getLoaderId() {
        return BlueTools.id("resources");
    }

}
