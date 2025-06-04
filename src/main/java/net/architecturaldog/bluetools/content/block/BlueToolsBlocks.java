package net.architecturaldog.bluetools.content.block;

import dev.jaxydog.lodestone.api.AutoLoader;
import net.architecturaldog.bluetools.BlueTools;
import net.architecturaldog.bluetools.content.block.custom.ForgeInterfaceBlock;
import net.minecraft.block.AbstractBlock;
import net.minecraft.util.Identifier;

public final class BlueToolsBlocks extends AutoLoader {

    public static final ForgeInterfaceBlock FORGE_INTERFACE = new ForgeInterfaceBlock(
        "forge_interface",
        AbstractBlock.Settings.create()
    );

    @Override
    public Identifier getLoaderId() {
        return BlueTools.id("blocks");
    }

}
